package com.example.plugins

import com.example.routing.associationRoutes
import com.example.routing.forumRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDate

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Whisker Watch API🐱")
        }
    }

    associationRoutes()
    forumRoutes()
}
