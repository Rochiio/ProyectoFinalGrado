package com.example

import com.example.plugins.*
import com.example.utils.Data
import io.ktor.server.application.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = EngineMain.main(args)
fun Application.module() = runBlocking{
    val dataUtil: Data by inject()

    configureKoin()
    configureSecurity()
    configureStorage()
    configureWebSockets()
    configureSerialization()
    configureRouting()
    configureCors()
    configureValidation()
    //configureSwagger()


    dataUtil.cleanData()
}
