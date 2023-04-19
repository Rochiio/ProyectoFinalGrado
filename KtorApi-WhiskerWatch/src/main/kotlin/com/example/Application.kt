package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureKoin()
    configureSecurity()
    configureWebSockets()
    configureSerialization()
    configureRouting()
    configureValidation()
    //configureSwagger()
}
