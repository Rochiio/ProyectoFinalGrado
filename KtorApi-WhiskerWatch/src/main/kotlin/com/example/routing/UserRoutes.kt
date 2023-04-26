package com.example.routing

import com.example.dto.UserCreateDto
import com.example.dto.UserLogin
import com.example.error.UserError
import com.example.mappers.toUserDto
import com.example.mappers.toUserWithToken
import com.example.models.users.Rol
import com.example.services.password.BcryptService
import com.example.services.token.TokenService
import com.example.services.users.UserService
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
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

            post("/register"){
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

            post("/login"){
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

                get(){
                    logger.info { "Get All User Route" }
                    val list = service.findAllUsers().map { it.toUserDto() }
                    call.respond(HttpStatusCode.OK, list)
                }

                get("/{id}"){
                    logger.info { "Get User By Id"}
                    val id = call.parameters["id"].toString()
                    service.findUserById(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it.toUserDto()) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }

                put("/{id}"){
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

                delete("/{id}"){
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