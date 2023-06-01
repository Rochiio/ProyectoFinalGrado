package com.example.routing

import com.example.dto.CalendarCreateDto
import com.example.models.calendar.Calendar
import com.example.models.users.Rol
import com.example.services.calendar.CalendarService
import com.example.validators.taskListValidation
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

private const val CAL = "/calendar"
fun Application.calendarRoutes(){
    val logger = KotlinLogging.logger{}
    val service: CalendarService by inject()


    routing {
        route(CAL){


            authenticate {


                get("", {
                  description = "Conseguir todos los calendarios"
                  response {
                      HttpStatusCode.OK to {
                          description = "Lista con todos los calendarios que hay almacenados"
                          body<List<Calendar>> { description = "Lista de calendarios"}
                      }
                  }
                }){

                    logger.info { "Get All Calendar Route" }
                    val list = service.findAllCalendars()
                    call.respond(HttpStatusCode.OK, list)
                }


                get("/{id}", {
                    description = "Conseguir calendario por el id"
                    request {
                        pathParameter<String>("id"){
                            description = "Id por el que buscar el calendario"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.OK to {
                            description = "Se ha encontrado el calendario"
                            body<Calendar> { description = "Calendario encontrado"}
                        }
                        HttpStatusCode.NotFound to { description= "No se ha encontrado el calendario" }
                    }
                }){

                    logger.info { "Get Calendar By Id"}
                    val id = call.parameters["id"].toString()
                    service.findCalendarById(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }


                get("/mapsId/{id}", {
                    description = "Conseguir un calendario por el id del mapa asociado"
                    request {
                        pathParameter<String>("id"){
                            description = "Id del mapa por el que buscar el calendario"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.OK to {
                            description = "Se ha encontrado el calednario"
                            body<Calendar> {description="Calendario encontrado"}
                        }
                        HttpStatusCode.NotFound to { description = "No se ha encontrado el calendario con el id del mapa"}
                    }
                }){

                    logger.info { "Get Calendar By Maps Id"}
                    val id = call.parameters["id"].toString()
                    service.findCalendarByMapsId(id)
                        .onSuccess { call.respond(HttpStatusCode.OK, it) }
                        .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                }

                post("", {
                  description = "Salvar un calendario"
                  request {
                      body<CalendarCreateDto> { description = "Datos necesarios para crear el calendario"}
                  }
                  response {
                      HttpStatusCode.Created to {
                          description = "Calendario creado correctamente"
                          body<Calendar> { description = "Calendario creado"}
                      }
                      HttpStatusCode.BadRequest to { description= "Validacion de datos incorrecta"}
                  }
                }){

                    logger.info { "Save Calendar" }
                    try{
                        val post = call.receive<CalendarCreateDto>()
                        val correct = if (post.listTasks.isNotEmpty()) post.taskListValidation() else true

                        if(correct) {
                            service.saveCalendar(post)
                                .onSuccess { call.respond(HttpStatusCode.Created, it) }
                        }else{
                            call.respond(HttpStatusCode.BadRequest, "Tareas incorrectos")
                        }
                    }catch (e: RequestValidationException){
                        call.respond(HttpStatusCode.BadRequest, e.message.toString())
                    }
                }


                put("/{id}", {
                    description = "Actualizar el calendario"
                    request {
                        pathParameter<String>("id"){
                            description = "Id del calendario a actualizar"
                            required = true
                        }
                        body<CalendarCreateDto> { description = "Datos necesarios para actualizar el calendario" }
                    }
                    response {
                        HttpStatusCode.Created to {
                            description = "Calendario actualizado correctamenete"
                            body<Calendar> { description = "Calendario actualizado"}
                        }
                        HttpStatusCode.BadRequest to { description = "Validacion de datos incorrecto"}
                        HttpStatusCode.NotFound to { description = "No se ha encontrado el calendario"}
                    }
                }){

                    logger.info { "Update Calendar" }
                    try {
                        val id = call.parameters["id"].toString()
                        val put = call.receive<CalendarCreateDto>()
                        val correct = if (put.listTasks.isNotEmpty()) put.taskListValidation() else true

                        if(correct) {
                            service.updateCalendar(put, id)
                                .onSuccess { call.respond(HttpStatusCode.Created, it) }
                                .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                        }else{
                            call.respond(HttpStatusCode.BadRequest, "Tareas incorrectos")
                        }
                    }catch (e: RequestValidationException){
                        call.respond(HttpStatusCode.BadRequest, e.reasons.toString())
                    }
                }


                delete("/{id}", {
                    description = "Eliminar un calendario por su ID"
                    request {
                        pathParameter<String>("id"){
                            description = "Id por el que buscar el calendario"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.NoContent to { description = "Se ha eliminado correctamente el calendario"}
                        HttpStatusCode.NotFound to { description = "No se ha encontrado el calendario a eliminar"}
                    }
                }){

                    logger.info { "Delete Calendar" }
                    val id = call.parameters["id"].toString()
                    val auth = call.principal<JWTPrincipal>()
                    val rol = Rol.valueOf(auth?.payload?.getClaim("rol").toString().replace("\"", ""))
                    if(rol != Rol.ADMIN){
                        call.respond(HttpStatusCode.Unauthorized, "No tienes permisos para realizar esta acción")
                    }else {
                        service.deleteCalendar(id)
                            .onSuccess { call.respond(HttpStatusCode.NoContent) }
                            .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                    }
                }


                delete("/mapsId/{id}", {
                    description = "Eliminar un calendario por el id del mapa asociado"
                    request {
                        pathParameter<String>("id"){
                            description = "Id del mapa por el que eliminar el calendario"
                            required = true
                        }
                    }
                    response {
                        HttpStatusCode.NoContent to { description = "Se ha elimiando correctamente el calendario" }
                        HttpStatusCode.NotFound to { description = "No se ha encontrado el calendario" }
                    }
                }){

                    logger.info { "Delete Calendar By Maps Id" }
                    val id = call.parameters["id"].toString()
                    val auth = call.principal<JWTPrincipal>()
                    val rol = Rol.valueOf(auth?.payload?.getClaim("rol").toString().replace("\"", ""))
                    if(rol != Rol.ADMIN){
                        call.respond(HttpStatusCode.Unauthorized, "No tienes permisos para realizar esta acción")
                    }else {
                        service.deleteCalendarByMapsId(id)
                            .onSuccess { call.respond(HttpStatusCode.NoContent) }
                            .onFailure { call.respond(HttpStatusCode.NotFound, it.message) }
                    }
                }

            }

        }
    }
}