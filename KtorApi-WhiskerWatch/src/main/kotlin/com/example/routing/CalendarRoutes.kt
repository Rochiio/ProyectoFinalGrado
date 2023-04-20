package com.example.routing

import com.example.dto.CalendarCreateDto
import com.example.services.calendar.CalendarService
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

private const val CAL = "/calendar"
fun Application.calendarRoutes(){
    val logger = KotlinLogging.logger{}
    val service: CalendarService by inject()


    routing {
        route(CAL){

            authenticate {

                get(){
                    logger.info { "Get All Calendar Route" }
                    val list = service.findAllCalendars()
                    call.respond(HttpStatusCode.OK, list)
                }

                get("/{id}"){
                    logger.info { "Get Calendar By Id"}
                    val id = call.parameters["id"].toString()
                    service.findCalendarById(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }

                get("/mapsId/{id}"){
                    logger.info { "Get Calendar By Maps Id"}
                    val id = call.parameters["id"].toString()
                    service.findCalendarByMapsId(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }

                post(){
                    logger.info { "Save Calendar" }
                    try{
                        val post = call.receive<CalendarCreateDto>()
                        service.saveCalendar(post)
                            .onSuccess { call.respond(HttpStatusCode.Created, it) }
                    }catch (e: RequestValidationException){
                        call.respond(HttpStatusCode.BadRequest, e.message.toString())
                    }
                }

                put("/{id}"){
                    logger.info { "Update Calendar" }
                    try {
                        val id = call.parameters["id"].toString()
                        val put = call.receive<CalendarCreateDto>()
                        service.updateCalendar(put, id)
                            .onSuccess { call.respond(HttpStatusCode.Created, it) }
                            .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                    }catch (e: RequestValidationException){
                        call.respond(HttpStatusCode.BadRequest, e.reasons.toString())
                    }
                }

                delete("/{id}"){
                    logger.info { "Delete Calendar" }
                    val id = call.parameters["id"].toString()
                    service.deleteCalendar(id)
                        .onSuccess { call.respond(HttpStatusCode.NoContent) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }

                delete("/mapsId/{id}"){
                    logger.info { "Delete Calendar By Maps Id" }
                    val id = call.parameters["id"].toString()
                    service.deleteCalendarByMapsId(id)
                        .onSuccess { call.respond(HttpStatusCode.NoContent) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }
            }

        }
    }
}