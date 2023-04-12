package com.example.routing

import com.example.models.forum.Forum
import com.example.models.forum.ForumMessages
import com.example.repositories.forum.ForumRepositoryImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Application.forumRoutes(){
    val API = "/forum"
    val repository: ForumRepositoryImpl by inject()

    routing {
        route(API){

            get(){
                val creado = Forum(mapsUuid = UUID.randomUUID().toString(), listMessages = mutableListOf(ForumMessages(username = "paco", message = "Este es un mensaje de prueba")))
                val devolver = repository.save(creado)
                call.respond(HttpStatusCode.OK, devolver)
            }


        }
    }
}