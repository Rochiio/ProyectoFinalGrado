package com.example.routing

import com.example.repositories.users.AssociationRepositoryImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.toList
import org.koin.ktor.ext.inject

fun Application.associationRoutes(){
val API = "/association"
val repo: AssociationRepositoryImpl by inject()


    routing {
        route(API){

            get(){
                //val aso = Association(name = "Pruba", email = "pruba@gmail.com", username = "pruba", password = "123456"
                //    , description = "xxxxx", url = "http://google.com")
                //var salvado = repo.save(aso)
                val lista = repo.findAll().toList()
                call.respond(HttpStatusCode.OK, lista)
            }


        }
    }
}