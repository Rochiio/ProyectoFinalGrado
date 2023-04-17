package com.example.plugins

import com.example.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Whisker Watch API🐱")
        }
    }

    associationRoutes()
    userRoutes()
    mapRoutes()
    forumRoutes()
    calendarRoutes()
}
