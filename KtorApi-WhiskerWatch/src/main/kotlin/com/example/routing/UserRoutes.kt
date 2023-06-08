package com.example.routing

import com.example.dto.UserCreateDto
import com.example.dto.UserDto
import com.example.dto.UserLogin
import com.example.dto.UserWithTokenDto
import com.example.error.UserError
import com.example.mappers.toUserDto
import com.example.mappers.toUserWithToken
import com.example.models.users.Rol
import com.example.services.password.BcryptService
import com.example.services.token.TokenService
import com.example.services.users.UserService
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

private const val US = "/user"
fun Application.userRoutes(){
    val logger = KotlinLogging.logger{}
    val service: UserService by inject()
    val tokenService: TokenService by inject()
    val passwordEncoder: BcryptService by inject()


    routing {
        route(US){


            post("/register", {
                description =" Registro usuario"
                request {
                    body<UserCreateDto> { description = "Datos necesarios para registrar a el usuario"}
                }
                response {
                    HttpStatusCode.Created to {
                        description = "El usuario se ha creado correctamente"
                        body<UserWithTokenDto> { description = "Usuario creado con el token"}
                    }
                    HttpStatusCode.BadRequest to { description = "Validacion de datos incorrecta"}
                }
            }){

                logger.info { "Register User" }
                try {
                    val post = call.receive<UserCreateDto>()
                    service.saveUser(post)
                        .onSuccess {
                            val token = tokenService.generateTokenUser(it)
                            call.respond(HttpStatusCode.Created, it.toUserWithToken(token))
                        }
                        .onFailure { call.respond(HttpStatusCode.BadRequest, it.message) }
                }catch (e: RequestValidationException){
                    call.respond(HttpStatusCode.BadRequest, e.reasons.toString())
                }
            }


            post("/login", {
                description = "Inicio de sesion usuario"
                request {
                    body<UserLogin> {description = "Datos necesarios para el inicio de sesion"}
                }
                response {
                    HttpStatusCode.OK to {
                        description = "Inicio de sesion del usuario correcto"
                        body<UserWithTokenDto> { description = "Usuario con el token"}
                    }
                    HttpStatusCode.BadRequest to { description = "Validacion de datos del usuario incorrecto"}
                    HttpStatusCode.NotFound to { description = "No se ha encontrado un usuario con esos datos de inicio de sesion"}
                }
            }){

                logger.info { "Login User" }
                try{
                    val post = call.receive<UserLogin>()
                    service.findUserByEmail(post.email)
                        .onSuccess {
                            if(passwordEncoder.verifyPassword(post.password, it.password)){
                                val token = tokenService.generateTokenUser(it)
                                call.respond(HttpStatusCode.OK, it.toUserWithToken(token))
                            }else{
                                call.respond(HttpStatusCode.NotFound, "Correo o contraseña incorrectos")
                            }
                        }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }catch (e: RequestValidationException){
                    call.respond(HttpStatusCode.BadRequest, e.reasons.toString())
                }
            }


            authenticate {


                get("", {
                    description = "Conseguir todos los usuarios almacenados"
                    response {
                        HttpStatusCode.OK to {
                            description = "Lista de todos los usuarios almacenados"
                            body<List<UserDto>> { description = "Lista de usuarios"}
                        }
                    }
                }){

                    logger.info { "Get All User Route" }
                    val list = service.findAllUsers().map { it.toUserDto() }
                    call.respond(HttpStatusCode.OK, list)
                }


                get("/{id}", {
                    description = "Conseguir usuario por el ID"
                    request {
                        pathParameter<String>("id"){
                            description = "Id por el que buscar a el usuario"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.OK to {
                            description = "Se ha encontrado correctamenete a el usuario"
                            body<UserDto> { description = "Usuario encontrado"}
                        }
                        HttpStatusCode.NotFound to { description = "No se ha encontrado a el usuario"}
                    }
                }){

                    logger.info { "Get User By Id"}
                    val id = call.parameters["id"].toString()
                    service.findUserById(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it.toUserDto()) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }


                put("/{id}", {
                    description = "Actualizar usuario"
                    request {
                        pathParameter<String>("id"){
                            description = "Id del usuario a actualizar"
                            required = true
                        }
                        body<UserCreateDto> { description ="Datos necesarios para actualizar el usuario"}
                    }
                    response {
                        HttpStatusCode.Created to {
                            description = "Se ha actualizado correctamente el usuario"
                            body<UserDto> { description ="Usuario actualizado"}
                        }
                        HttpStatusCode.NotFound to { description = "No se ha encontrado a el usuario a actualizar"}
                        HttpStatusCode.BadRequest to { description = "Validacion de datos incorrecta"}
                    }
                }){

                    logger.info { "Update User" }
                    try {
                        val id = call.parameters["id"].toString()
                        val put = call.receive<UserCreateDto>()
                        val auth = call.principal<JWTPrincipal>()
                        val rol = Rol.valueOf(auth?.payload?.getClaim("rol").toString().replace("\"", ""))
                        if (rol == Rol.ASSOCIATION){
                            call.respond(HttpStatusCode.Unauthorized, "No tienes permisos para realizar esta acción")
                        }else {
                            service.updateUser(put, id)
                                .onSuccess { call.respond(HttpStatusCode.Created, it.toUserDto()) }
                                .onFailure {
                                    when (it) {
                                        is UserError.UserBadRequestError -> call.respond(
                                            HttpStatusCode.BadRequest,
                                            it.message
                                        )

                                        is UserError.UserNotFoundError -> call.respond(
                                            HttpStatusCode.NotFound,
                                            it.message
                                        )
                                    }
                                }
                        }
                    }catch (e: RequestValidationException){
                        call.respond(HttpStatusCode.BadRequest, e.reasons.toString())
                    }
                }


                delete("/{id}", {
                    description = "Eliminar usuario"
                    request {
                        pathParameter<String>("id"){
                            description = "Id por el que buscar el usuario a eliminar"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.NoContent to { description = "Se ha eliminado correctamente el usuario"}
                        HttpStatusCode.NotFound to { description = "No se ha encontrado el usuario a eliminar"}
                    }
                }){

                    logger.info { "Delete User" }
                    val id = call.parameters["id"].toString()
                    val auth = call.principal<JWTPrincipal>()
                    val rol = Rol.valueOf(auth?.payload?.getClaim("rol").toString().replace("\"", ""))
                    if(rol == Rol.ASSOCIATION){
                        call.respond(HttpStatusCode.Unauthorized, "No tienes permisos para realizar esta acción")
                    }else {
                        service.deleteUser(id)
                            .onSuccess { call.respond(HttpStatusCode.NoContent) }
                            .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                    }
                }

            }

        }
    }
}