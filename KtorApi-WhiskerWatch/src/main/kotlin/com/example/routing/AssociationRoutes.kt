package com.example.routing

import com.example.dto.AssociationCreateDto
import com.example.dto.AssociationLogin
import com.example.error.AssociationError
import com.example.mappers.toAssociationDto
import com.example.mappers.toAssociationTokenDto
import com.example.services.password.BcryptService
import com.example.services.token.TokenService
import com.example.services.users.AssociationService
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mu.KotlinLogging
import org.koin.ktor.ext.inject

private const val ASO = "/association"
fun Application.associationRoutes(){
    val logger = KotlinLogging.logger{}
    val service: AssociationService by inject()
    val tokenService: TokenService by inject()
    val passwordEncoder: BcryptService by inject()


    routing {
        route(ASO){

            post("/register"){
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

            post("/login"){
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
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }catch (e: RequestValidationException){
                    call.respond(HttpStatusCode.BadRequest, e.reasons.toString())
                }
            }

            authenticate {

                get(){
                    logger.info { "Get All Association Route" }
                    //val aso = Association(name = "Pruba", email = "pruba@gmail.com", username = "pruba", password = "123456"
                    //    , description = "xxxxx", url = "http://google.com")
                    //var salvado = repo.save(aso)
                    val list = service.findAllAssociations().map { it.toAssociationDto() }
                    call.respond(HttpStatusCode.OK, list)
                }

                get("/{id}"){
                    logger.info { "Get Association By Id"}
                    val id = call.parameters["id"].toString()
                    service.findAssociationById(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it.toAssociationDto()) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }

//                post(){
//                    logger.info { "Save Association" }
//                    try{
//                        val post = call.receive<AssociationCreateDto>()
//                        service.saveAssociation(post)
//                            .onSuccess { call.respond(HttpStatusCode.Created, it.toAssociationDto()) }
//                            .onFailure { when(it){
//                                is AssociationError.AssociationBadRequestError -> call.respond(HttpStatusCode.BadRequest, it.message)
//                                is AssociationError.AssociationNotFoundError -> call.respond(HttpStatusCode.NotFound, it.message)
//                            } }
//                    }catch (e: RequestValidationException){
//                        call.respond(HttpStatusCode.BadRequest, e.message.toString())
//                    }
//                }

                put("/{id}"){
                    logger.info { "Update Association" }
                    try {
                        val id = call.parameters["id"].toString()
                        val put = call.receive<AssociationCreateDto>()
                        service.updateAssociation(put, id)
                            .onSuccess { call.respond(HttpStatusCode.Created, it.toAssociationDto()) }
                            .onFailure { when(it){
                                    is AssociationError.AssociationBadRequestError -> call.respond(HttpStatusCode.BadRequest, it.message)
                                    is AssociationError.AssociationNotFoundError -> call.respond(HttpStatusCode.NotFound, it.message)
                                } }
                    }catch (e: RequestValidationException){
                        call.respond(HttpStatusCode.BadRequest, e.reasons.toString())
                    }
                }

                delete("/{id}"){
                    logger.info { "Delete Association" }
                    val id = call.parameters["id"].toString()
                    service.deleteAssociation(id)
                        .onSuccess { call.respond(HttpStatusCode.NoContent) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }

            }

        }
    }
}