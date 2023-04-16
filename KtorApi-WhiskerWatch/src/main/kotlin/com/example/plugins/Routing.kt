package com.example.plugins

import com.example.routing.associationRoutes
import com.example.routing.forumRoutes
import com.example.routing.userRoutes
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
    forumRoutes()
}
