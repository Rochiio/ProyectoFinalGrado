package com.example.plugins

import com.example.validators.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation(){
    install(RequestValidation) {
        userValidation()
        associationValidation()
        mapsValidation()
        forumValidation()
        calendarValidation()
    }
}