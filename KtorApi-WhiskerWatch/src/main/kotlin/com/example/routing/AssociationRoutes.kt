package com.example.routing

import com.example.dto.AssociationCreateDto
import com.example.dto.AssociationDto
import com.example.dto.AssociationLogin
import com.example.dto.AssociationTokenDto
import com.example.error.AssociationError
import com.example.mappers.toAssociationDto
import com.example.mappers.toAssociationTokenDto
import com.example.models.users.Rol
import com.example.services.password.BcryptService
import com.example.services.token.TokenService
import com.example.services.users.AssociationService
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import io.github.smiley4.ktorswaggerui.dsl.delete
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.put
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mu.KotlinLogging
import org.koin.ktor.ext.inject
import java.io.File

private const val ASO = "/association"
fun Application.associationRoutes(){
    val logger = KotlinLogging.logger{}
    val service: AssociationService by inject()
    val tokenService: TokenService by inject()
    val passwordEncoder: BcryptService by inject()


    routing {
        route(ASO){


            post("/register", {
                description = "Registro Asociaciones"
                request {
                    body<AssociationCreateDto> { description = "Datos de la asociacion necesarios para el registro" }
                }
                response {
                    HttpStatusCode.Created to {
                        description = "Asociacion Creada"
                        body<AssociationTokenDto> {description="Asociacion creada con el token"}
                    }
                    HttpStatusCode.BadRequest to { description = "Validación de la asociacion incorrecto" }
                }
            }){

                logger.info { "Register Association" }
                try {
                    val post = call.receive<AssociationCreateDto>()
                    service.saveAssociation(post)
                        .onSuccess {
                            val token = tokenService.generateTokenAssociation(it)
                            call.respond(HttpStatusCode.Created, it.toAssociationTokenDto(token))
                        }
                        .onFailure { call.respond(HttpStatusCode.BadRequest, it.message) }
                }catch (e: RequestValidationException){
                    call.respond(HttpStatusCode.BadRequest, e.reasons.toString())
                }
            }


            post("/login", {
                description = "Login Asociaciones"
                request {
                    body<AssociationLogin> {description="Datos necesarios para el inicio de sesion de la asociacion"}
                }
                response {
                    HttpStatusCode.OK to {
                        description = "Se ha encontrado una cuenta asociada"
                        body<AssociationTokenDto> {description="Asociacion con el token"}
                    }

                    HttpStatusCode.NotFound to { description = "No se ha encontrado una asociacion con esos datos" }
                }
            }){

                logger.info { "Login Association" }
                try{
                    val post = call.receive<AssociationLogin>()
                    service.findAssociationByEmail(post.email)
                        .onSuccess {
                            if(passwordEncoder.verifyPassword(post.password, it.password)){
                                val token = tokenService.generateTokenAssociation(it)
                                call.respond(HttpStatusCode.OK, it.toAssociationTokenDto(token))
                            }else{
                                call.respond(HttpStatusCode.NotFound, "Correo o contraseña incorrectos")
                            }
                        }
                        .onFailure { call.respond(HttpStatusCode.Found, it.message) }
                }catch (e: RequestValidationException){
                    call.respond(HttpStatusCode.BadRequest, e.reasons.toString())
                }
            }

            authenticate {


                get("", {
                    description = "Conseguir todas las asociaciones"
                    response {
                        HttpStatusCode.OK to {
                            description = "Toda la lista de asociaciones que hay en el programa"
                            body<List<AssociationDto>>{ description = "Lista de las asociaciones"}
                        }
                    }
                }) {

                    logger.info { "Get All Association Route" }
                    val list = service.findAllAssociations().map { it.toAssociationDto() }
                    call.respond(HttpStatusCode.OK, list)
                }


                get("/{id}", {
                    description = "Encontrar una asociacion por su ID"
                    request {
                        pathParameter<String>("id") {
                            description = "Id por el que buscar la asociacion"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.OK to {
                            description = "Se ha encontrado la asociacion"
                            body<AssociationDto>{description = "Asociacion encontrada"}
                        }
                        HttpStatusCode.NotFound to {description = "No se ha encontrado la asociacion"}
                    }
                }){

                    logger.info { "Get Association By Id"}
                    val id = call.parameters["id"].toString()
                    service.findAssociationById(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it.toAssociationDto()) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }


                put("/{id}", {
                    description = "Actualizar Asociacion"
                    request {
                        pathParameter<String>("id"){
                            description = "Id por el que buscar la asociacion a actualizar"
                            required = true
                        }
                        body<AssociationDto>{description = "Datos necesarios para actualizar la asociacion"}
                    }
                    response {
                        HttpStatusCode.Created to {
                            description = "Se ha actualizado correctamente la asociacion"
                            body<AssociationDto>{description = "Asociacion actualizada"}
                        }
                        HttpStatusCode.NotFound to {description = "No se ha encontrado la asociacion"}
                        HttpStatusCode.BadRequest to {description = "Validacion de datos incorrecta"}
                        HttpStatusCode.Found to {description = "Ya existe otra asociacion con el email de los datos"}
                    }
                }){

                    logger.info { "Update Association" }
                    try {
                        val id = call.parameters["id"].toString()
                        val auth = call.principal<JWTPrincipal>()
                        val rol = Rol.valueOf(auth?.payload?.getClaim("rol").toString().replace("\"", ""))
                        val put = call.receive<AssociationCreateDto>()
                        if (rol == Rol.USER) {
                            call.respond(HttpStatusCode.Unauthorized, "No tienes permisos para realizar esta acción")
                        } else {
                            service.updateAssociation(put, id)
                                .onSuccess { call.respond(HttpStatusCode.Created, it.toAssociationDto()) }
                                .onFailure {
                                    when (it) {
                                        is AssociationError.AssociationFoundError -> call.respond(
                                            HttpStatusCode.Found,
                                            it.message
                                        )

                                        is AssociationError.AssociationNotFoundError -> call.respond(
                                            HttpStatusCode.NotFound,
                                            it.message
                                        )

                                        is AssociationError.AssociationBadRequestError -> call.respond(
                                            HttpStatusCode.BadRequest,
                                            it.message
                                        )
                                    }
                                }
                        }
                    } catch (e: RequestValidationException){
                        call.respond(HttpStatusCode.BadRequest, e.reasons.toString())
                    }
                }


                delete("/{id}", {
                    description = "Eliminar Asociacion"
                    request {
                        pathParameter<String>("id"){
                            description = "Id de la asociacion a eliminar"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.NoContent to { description = "Se ha eliminado correctamente la asociacion"}
                        HttpStatusCode.NotFound to { description = "No se ha encontrado la asociacion a eliminar"}
                    }
                }){
                    logger.info { "Delete Association" }
                    val id = call.parameters["id"].toString()
                    val auth = call.principal<JWTPrincipal>()
                    val rol = Rol.valueOf(auth?.payload?.getClaim("rol").toString().replace("\"", ""))
                    if(rol == Rol.USER){
                        call.respond(HttpStatusCode.Unauthorized, "No tienes permisos para realizar esta acción")
                    }else {
                        service.deleteAssociation(id)
                            .onSuccess { call.respond(HttpStatusCode.NoContent) }
                            .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                    }
                }


                post("/image/{id}", {
                    description = "Añadir una imagen a la asociacion"
                    request {
                        pathParameter<String>("id") {
                            description = "Id de la asociacion a la que añadir la imagen"
                            required = true
                        }
                        body<MultiPartData> {description = "Imagen a añadir"}
                    }
                    response {
                        HttpStatusCode.Created to {
                            description = "Se ha creado/almacenado correctamente la imagen"
                            body<String> { description = "Datos del almacenado de la imagen"}
                        }
                        HttpStatusCode.NotFound to {description = "No se ha encontrado la asociacion"}
                    }
                }){

                    logger.info{"Save image to Association"}
                    val id = call.parameters["id"].toString()
                    val auth = call.principal<JWTPrincipal>()
                    val rol = Rol.valueOf(auth?.payload?.getClaim("rol").toString().replace("\"", ""))
                    val multipart = call.receiveMultipart()
                    if(rol == Rol.USER){
                        call.respond(HttpStatusCode.Unauthorized, "No tienes permisos para realizar esta acción")
                    }else {
                        try {
                            multipart.forEachPart { partData ->
                                if (partData is PartData.FileItem) {
                                    service.changeImageAssociation(partData, id)
                                        .onSuccess {call.respond(HttpStatusCode.Created, it) }
                                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                                }
                            }
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.BadRequest, "Problemas para guardar la imagen")
                        }
                    }
                }


                get("/image/{id}", {
                    description = "Conseguit una imagen por el id de la asociacion"
                    request {
                        pathParameter<String>("id"){
                            description = "Id de la asociacion"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.OK to {
                            description = "Se ha encontrado correctamente la imagen de la asociacion"
                            body<File> {description = "Fichero encontrado"}
                        }
                        HttpStatusCode.NotFound to { description = "No se ha encontrado la asociacion o la imagen"}
                        HttpStatusCode.BadRequest to { description = "Problemas para conseguir la imagen"}
                    }
                }){

                    logger.info { "Get Image by Association Id" }
                    val id = call.parameters["id"].toString()
                    service.getImageAssociation(id)
                        .onSuccess {
                            call.respondFile(it)
                        }
                        .onFailure { when(it){
                            is AssociationError.AssociationBadRequestError -> call.respond(HttpStatusCode.BadRequest, it.message)
                            is AssociationError.AssociationFoundError -> call.respond(HttpStatusCode.Found, it.message)
                            is AssociationError.AssociationNotFoundError -> call.respond(HttpStatusCode.NotFound, it.message)
                        }
                    }
                }


                delete("/image/{id}", {
                    description = "Eliminar una imagen"
                    request {
                        pathParameter<String>("id"){
                            description = "Id de la asociacion por la que buscar para eliminar la imagen asignada a la asociacion"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.NoContent to { description = "Se ha eliminado correctamente la imagen"}
                        HttpStatusCode.NotFound to { description = "No se ha encontrado la asociacion o no se ha encontrado la imagen"}
                        HttpStatusCode.BadRequest to  { description = "Problemas para eliminar la imagen"}
                    }
                }){

                    logger.info { "Delete Image by Association Id" }
                    val id = call.parameters["id"].toString()
                    val auth = call.principal<JWTPrincipal>()
                    val rol = Rol.valueOf(auth?.payload?.getClaim("rol").toString().replace("\"", ""))
                    if(rol == Rol.USER){
                        call.respond(HttpStatusCode.Unauthorized, "No tienes permisos para realizar esta acción")
                    }else {
                        service.deleteImageAssociation(id)
                            .onSuccess {
                                call.respond(HttpStatusCode.NoContent)
                            }
                            .onFailure {
                                when (it) {
                                    is AssociationError.AssociationBadRequestError -> call.respond(
                                        HttpStatusCode.BadRequest,
                                        it.message
                                    )

                                    is AssociationError.AssociationFoundError -> call.respond(
                                        HttpStatusCode.Found,
                                        it.message
                                    )

                                    is AssociationError.AssociationNotFoundError -> call.respond(
                                        HttpStatusCode.NotFound,
                                        it.message
                                    )
                                }
                            }
                    }
                }


            }


        }
    }
}