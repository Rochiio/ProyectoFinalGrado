package com.example.routing

import com.example.dto.MapsCreateDto
import com.example.mappers.toMapsDto
import com.example.services.maps.MapsService
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

private const val MAP = "/map"
fun Application.mapRoutes(){
    val logger = KotlinLogging.logger{}
    val service: MapsService by inject()


    routing {
        route(MAP){

            authenticate {

                get(){
                    logger.info { "Get All Map Route" }
                    val list = service.findAllMaps().map { it.toMapsDto() }
                    call.respond(HttpStatusCode.OK, list)
                }

                get("/{id}"){
                    logger.info { "Get Map By Id"}
                    val id = call.parameters["id"].toString()
                    service.findMapById(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it.toMapsDto()) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }

                post(){
                    logger.info { "Save Map" }
                    try{
                        val post = call.receive<MapsCreateDto>()
                        service.saveMap(post)
                            .onSuccess { call.respond(HttpStatusCode.Created, it.toMapsDto()) }
                    }catch (e: RequestValidationException){
                        call.respond(HttpStatusCode.BadRequest, e.message.toString())
                    }
                }

                put("/{id}"){
                    logger.info { "Update Map" }
                    try {
                        val id = call.parameters["id"].toString()
                        val put = call.receive<MapsCreateDto>()
                        service.updateMap(put, id)
                            .onSuccess { call.respond(HttpStatusCode.Created, it.toMapsDto()) }
                            .onFailure { call.respond(HttpStatusCode.NotFound, it.message.toString()) }
                    }catch (e: RequestValidationException){
                        call.respond(HttpStatusCode.BadRequest, e.reasons.toString())
                    }
                }

                delete("/{id}"){
                    logger.info { "Delete Map" }
                    val id = call.parameters["id"].toString()
                    service.deleteMap(id)
                        .onSuccess { call.respond(HttpStatusCode.NoContent) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }

            }

        }
    }
}