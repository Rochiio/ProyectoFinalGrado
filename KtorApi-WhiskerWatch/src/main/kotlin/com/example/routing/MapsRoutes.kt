package com.example.routing

import com.example.dto.CalendarCreateDto
import com.example.dto.ForumCreateDto
import com.example.dto.MapsCreateDto
import com.example.models.Maps
import com.example.models.users.Rol
import com.example.services.calendar.CalendarService
import com.example.services.forum.ForumService
import com.example.services.maps.MapsService
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


                get("", {
                    description = "Conseguir todas las localizaciones"
                    response {
                        HttpStatusCode.OK to {
                            description = "Lista de todas las localizaciones almacenadas"
                            body<List<Maps>> { description = "Lista de localizaciones"}
                        }
                    }
                }){

                    logger.info { "Get All Map Route" }
                    val list = service.findAllMaps()
                    call.respond(HttpStatusCode.OK, list)
                }


                get("/{id}", {
                    description = "Conseguir localizacion por ID"
                    request {
                        pathParameter<String>("id"){
                            description = "Id por el que buscar la localizacion"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.OK to {
                            description = "Se ha encontrado la localizacion"
                            body<Maps> { description = "Localizacion encontrada"}
                        }
                        HttpStatusCode.NotFound to { description = "No se ha encontrado la localizacion"}
                    }
                }){

                    logger.info { "Get Map By Id"}
                    val id = call.parameters["id"].toString()
                    service.findMapById(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }


                post("", {
                    description = "Salvar localizacion"
                    request {
                        body<MapsCreateDto> { description = "Datos necesarios para crear la localizacion" }
                    }
                    response {
                        HttpStatusCode.Created to {
                            description = "Se ha creado la localizacion correctamente"
                            body<Maps>{ description = "Localizacion creada"}
                        }
                        HttpStatusCode.BadRequest to { description = "Validacion de datos incorrecta"}
                    }
                }){

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


                put("/{id}", {
                    description = "Actualizar una localizacion"
                    request {
                        pathParameter<String>("id"){
                            description = "Id de la localizacion a buscar para actualizar"
                            required = true
                        }
                        body<MapsCreateDto> { description = "Datos necesarios para actualizar la localizacion" }
                    }
                    response {
                        HttpStatusCode.Created to {
                            description = "Se ha actualizado la localizacion correctamente"
                            body<Maps> { description = "Localizacion actualizada"}
                        }
                        HttpStatusCode.NotFound to { description = "No se ha encontrado la localizacion a actualizar"}
                        HttpStatusCode.BadRequest to { description = "Validacion de datos incorrecta"}
                    }
                }){

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


                delete("/{id}", {
                    description = "Eliminar localizacion"
                    request {
                        pathParameter<String>("id"){
                            description = "Id por el que buscar la localizacion a eliminar"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.NoContent to { description = "Se ha eliminado la localizacion correctamente"}
                        HttpStatusCode.NotFound to { description = "No se ha encontrado la localizacion a eliminar"}
                    }
                }){

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


                delete("/adoption/{id}", {
                    description = "Eliminar una localizacion por recogida de colonia"
                    request {
                        pathParameter<String>("id"){
                            description = "Id por el que buscar la localizacion a eliminar"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.NoContent to { description = "Se ha eliminado la localizacion correctamente"}
                        HttpStatusCode.NotFound to { description = "No se ha encontrado la localizacion a eliminar"}
                    }
                }){

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