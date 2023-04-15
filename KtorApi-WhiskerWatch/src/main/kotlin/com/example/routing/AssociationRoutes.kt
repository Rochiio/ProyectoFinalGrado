package com.example.routing

import com.example.dto.AssociationCreateDto
import com.example.error.AssociationBadRequestException
import com.example.error.AssociationNotFoundException
import com.example.mappers.toAssociationDto
import com.example.services.users.AssociationService
import io.ktor.http.*
import io.ktor.server.application.*
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


    routing {
        route(ASO){

            get(){
                logger.info { "Get All Association Route" }
                //val aso = Association(name = "Pruba", email = "pruba@gmail.com", username = "pruba", password = "123456"
                //    , description = "xxxxx", url = "http://google.com")
                //var salvado = repo.save(aso)
                val list = service.findAllAssociations().map { it.toAssociationDto() }
                call.respond(HttpStatusCode.OK, list)
            }

            get("/{id}"){
                logger.info { "Get Association By Uuid"}
                try{
                    val id = call.parameters["id"].toString()
                    val find = service.findAssociationById(id)
                    call.respond(HttpStatusCode.OK, find.toAssociationDto())
                }catch (e: AssociationNotFoundException){
                    call.respond(HttpStatusCode.NotFound, e.message.toString())
                }
            }

            post("/register"){
                logger.info { "Register Association" }
                try {
                    val post = call.receive<AssociationCreateDto>()
                    val created = service.saveAssociation(post)
                    call.respond(HttpStatusCode.Created, created.toAssociationDto())
                }catch (e: RequestValidationException){
                    call.respond(HttpStatusCode.BadRequest, e.message.toString())
                }catch (e: AssociationBadRequestException){
                    call.respond(HttpStatusCode.BadRequest, e.message.toString())
                }
            }


        }
    }
}