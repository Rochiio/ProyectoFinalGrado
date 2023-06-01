package com.example.routing

import com.example.dto.ForumCreateDto
import com.example.models.forum.Forum
import com.example.models.users.Rol
import com.example.services.forum.ForumService
import com.example.validators.forumListValidation
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import io.github.smiley4.ktorswaggerui.dsl.delete
import io.github.smiley4.ktorswaggerui.dsl.get
import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.put
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mu.KotlinLogging
import org.koin.ktor.ext.inject


private const val FOR = "/forum"
fun Application.forumRoutes(){
    val logger = KotlinLogging.logger{}
    val service: ForumService by inject()


    routing {
        route(FOR){


            authenticate {


                get("", {
                    description = "Conseguir todos los foros"
                    response {
                        HttpStatusCode.OK to {
                            description = "Todos los foros almacenados"
                            body<List<Forum>> { description = "Lista de foros"}
                        }
                    }
                }){

                    logger.info { "Get All Forum Route" }
                    val list = service.findAllForums()
                    call.respond(HttpStatusCode.OK, list)
                }


                get("/{id}", {
                    description = "Conseguir un foro por el ID"
                    request {
                        pathParameter<String>("id"){
                            description = "Id por el que buscar el foro"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.OK to {
                            description = "Se ha encontrado el foro"
                            body<Forum> { description = "Foro encontrado"}
                        }
                        HttpStatusCode.NotFound to { description = "No se ha encontrado el foro" }
                    }
                }){

                    logger.info { "Get Forum By Id"}
                    val id = call.parameters["id"].toString()
                    service.findById(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }


                get("/mapsId/{id}", {
                    description = "Conseguir un foro por el id del mapa asociado"
                    request {
                        pathParameter<String>("id"){
                            description = "Id del mapa asociado al foro"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.OK to {
                            description = "Se ha encontrado el foro asociado a ese id del mapa"
                            body<Forum> { description = "Foro encontrado"}
                        }
                        HttpStatusCode.NotFound to { description = "No se ha encontrado el foro"}
                    }
                }){

                    logger.info { "Get Forum By Map Id"}
                    val id = call.parameters["id"].toString()
                    service.findByMapsId(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }


                post("" , {
                    description = "Salvar foro"
                    request{
                        body<ForumCreateDto> {description = "Datos necesarios para crear el foro"}
                    }
                    response {
                        HttpStatusCode.Created to {
                            description = "Se ha creado correctamente el foro"
                            body<Forum> { description = "Foro creado"}
                        }
                        HttpStatusCode.BadRequest to { description="Validacion de datos incorrecta"}
                    }
                }){

                    logger.info { "Save Forum" }
                    try{
                        val post = call.receive<ForumCreateDto>()
                        val correct = if (post.listMessages.isNotEmpty()) post.forumListValidation() else true

                        if (correct){
                            service.saveForum(post)
                                .onSuccess { call.respond(HttpStatusCode.Created, it) }
                        }else{
                            call.respond(HttpStatusCode.BadRequest, "Mensajes incorrectos")
                        }
                    }catch (e: RequestValidationException){
                        call.respond(HttpStatusCode.BadRequest, e.message.toString())
                    }
                }


                put("/{id}", {
                    description = "Actualizar foro"
                    request {
                        pathParameter<String>("id"){
                            description = "Id del foro a actualizar"
                            required = true
                        }
                        body<ForumCreateDto> {description="Datos necesarios para actualizar el foro"}
                    }
                    response {
                        HttpStatusCode.Created to {
                            description = "Foro actualizado correctamente"
                            body<Forum> { description = "Foro actualizado"}
                        }
                        HttpStatusCode.NotFound to { description = "No se ha encontrado el foro"}
                        HttpStatusCode.BadRequest to { description = "Validacion de datos incorrecta"}
                    }
                }){

                    logger.info { "Update Forum" }
                    try {
                        val id = call.parameters["id"].toString()
                        val put = call.receive<ForumCreateDto>()
                        val correct = if (put.listMessages.isNotEmpty()) put.forumListValidation() else true

                        if(correct) {
                            service.updateForum(put, id)
                                .onSuccess { call.respond(HttpStatusCode.Created, it) }
                                .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                        }else{
                            call.respond(HttpStatusCode.BadRequest, "Mensajes incorrectos")
                        }
                    }catch (e: RequestValidationException){
                        call.respond(HttpStatusCode.BadRequest, e.reasons.toString())
                    }
                }


                delete("/{id}", {
                    description = "Eliminar foro por el ID"
                    request {
                        pathParameter<String>("id"){
                            description = "Id por el que buscar el foro a eliminar"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.NoContent to { description = "Foro eliminado correctamente"}
                        HttpStatusCode.NotFound to { description = "No se ha encontrado el foro a eliminar"}
                    }
                }){

                    logger.info { "Delete Forum" }
                    val id = call.parameters["id"].toString()
                    val auth = call.principal<JWTPrincipal>()
                    val rol = Rol.valueOf(auth?.payload?.getClaim("rol").toString().replace("\"", ""))
                    if(rol != Rol.ADMIN){
                        call.respond(HttpStatusCode.Unauthorized, "No tienes permisos para realizar esta acción")
                    }else {
                        service.deleteForum(id)
                            .onSuccess { call.respond(HttpStatusCode.NoContent) }
                            .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                    }
                }


                delete("/mapsId/{id}", {
                    description = "Eliminar foro por el id del mapa asociado"
                    request {
                        pathParameter<String>("id"){
                            description = "Id del mapa por el que buscar el foro a eliminar"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.NoContent to { description = "Foro eliminado correctamente"}
                        HttpStatusCode.NotFound to { description = "No se ha encontrado el foro a eliminar"}
                    }
                }){

                    logger.info { "Delete Forum By Maps Id" }
                    val id = call.parameters["id"].toString()
                    val auth = call.principal<JWTPrincipal>()
                    val rol = Rol.valueOf(auth?.payload?.getClaim("rol").toString().replace("\"", ""))
                    if(rol != Rol.ADMIN){
                        call.respond(HttpStatusCode.Unauthorized, "No tienes permisos para realizar esta acción")
                    }else {
                        service.deleteForumByMapsId(id)
                            .onSuccess { call.respond(HttpStatusCode.NoContent) }
                            .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                    }
                }
            }

        }
    }

}