package com.example.routing

import com.example.dto.CalendarCreateDto
import com.example.dto.ForumCreateDto
import com.example.dto.MapsCreateDto
import com.example.models.users.Rol
import com.example.services.calendar.CalendarService
import com.example.services.forum.ForumService
import com.example.services.maps.MapsService
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
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.koin.ktor.ext.inject

private const val MAP = "/map"
fun Application.mapRoutes(){
    val logger = KotlinLogging.logger{}
    val service: MapsService by inject()
    val forumService: ForumService by inject()
    val calendarService: CalendarService by inject()


    routing {
        route(MAP){

            authenticate {

                get(){
                    logger.info { "Get All Map Route" }
                    val list = service.findAllMaps()
                    call.respond(HttpStatusCode.OK, list)
                }

                get("/{id}"){
                    logger.info { "Get Map By Id"}
                    val id = call.parameters["id"].toString()
                    service.findMapById(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }

                post(){
                    logger.info { "Save Map" }
                    try{
                        val post = call.receive<MapsCreateDto>()
                        service.saveMap(post)
                            .onSuccess {
                                launch { forumService.saveForum(ForumCreateDto(it.id, listOf())) }
                                launch { calendarService.saveCalendar(CalendarCreateDto(it.id, mutableListOf())) }
                                call.respond(HttpStatusCode.Created, it) }
                    }catch (e: RequestValidationException){
                        call.respond(HttpStatusCode.BadRequest, e.message.toString())
                    }
                }

                put("/{id}"){
                    logger.info { "Update Map" }
                    try {
                        val id = call.parameters["id"].toString()
                        val put = call.receive<MapsCreateDto>()
                        val auth = call.principal<JWTPrincipal>()
                        val rol = Rol.valueOf(auth?.payload?.getClaim("rol").toString().replace("\"", ""))
                        if(rol != Rol.ADMIN){
                            call.respond(HttpStatusCode.Unauthorized, "No tiene permisos para realizar esta acción")
                        }else {
                            service.updateMap(put, id)
                                .onSuccess { call.respond(HttpStatusCode.Created, it) }
                                .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                        }
                    }catch (e: RequestValidationException){
                        call.respond(HttpStatusCode.BadRequest, e.reasons.toString())
                    }
                }

                delete("/{id}"){
                    logger.info { "Delete Map" }
                    val id = call.parameters["id"].toString()
                    val auth = call.principal<JWTPrincipal>()
                    val rol = Rol.valueOf(auth?.payload?.getClaim("rol").toString().replace("\"", ""))
                    if( rol != Rol.ADMIN){
                        call.respond(HttpStatusCode.Unauthorized, "No tiene permisos para realizar esta acción")
                    }else {
                        service.deleteMap(id)
                            .onSuccess {
                                launch { forumService.deleteForumByMapsId(id) }
                                launch { calendarService.deleteCalendarByMapsId(id) }
                                call.respond(HttpStatusCode.NoContent) }
                            .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                    }
                }

                delete("/adoption/{id}"){
                    logger.info { "Delete Map Association Adoption" }
                    val id = call.parameters["id"].toString()
                    val auth = call.principal<JWTPrincipal>()
                    val rol = Rol.valueOf(auth?.payload?.getClaim("rol").toString().replace("\"", ""))
                    if(rol == Rol.USER){
                        call.respond(HttpStatusCode.Unauthorized, "No tienes permisos para realizar esta acción")
                    }else {
                        service.deleteMapsAdoption(id)
                            .onSuccess {
                                launch { forumService.deleteForumByMapsId(id) }
                                launch { calendarService.deleteCalendarByMapsId(id) }
                                call.respond(HttpStatusCode.NoContent) }
                            .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                    }
                }

            }

            webSocket("/notifications/adoption"){
                try{
                    service.addSuscriber(this.hashCode()) {
                        sendSerialized(it)
                    }

                    for (frame in incoming){
                        if(frame.frameType == FrameType.CLOSE){
                            break
                        }else if (frame is Frame.Text){
                            sendSerialized(frame.readText())
                        }
                    }
                }finally {
                    service.removeSuscriber(this.hashCode())
                }
            }

        }
    }
}