package com.example.routing

import com.example.dto.ForumCreateDto
import com.example.models.users.Rol
import com.example.services.forum.ForumService
import com.example.validators.forumListValidation
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


private const val FOR = "/forum"
fun Application.forumRoutes(){
    val logger = KotlinLogging.logger{}
    val service: ForumService by inject()


    routing {
        route(FOR){

            authenticate {

                get(){
                    logger.info { "Get All Forum Route" }
                    val list = service.findAllForums()
                    call.respond(HttpStatusCode.OK, list)
                }

                get("/{id}"){
                    logger.info { "Get Forum By Id"}
                    val id = call.parameters["id"].toString()
                    service.findById(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }

                get("/mapsId/{id}"){
                    logger.info { "Get Forum By Map Id"}
                    val id = call.parameters["id"].toString()
                    service.findByMapsId(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }

                post(){
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

                put("/{id}"){
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

                delete("/{id}"){
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

                delete("/mapsId/{id}"){
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