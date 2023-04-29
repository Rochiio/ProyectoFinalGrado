package com.example.validators

import com.example.dto.ForumCreateDto
import com.example.dto.ForumMessagesCreateDto
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.forumValidation(){
    validate<ForumCreateDto> { forum ->
        if (forum.mapsId.isBlank()){
            ValidationResult.Invalid("El id del mapa no puede estar vacío")
        }else {
            ValidationResult.Valid
        }
    }

    validate<ForumMessagesCreateDto> { message ->
        if (message.message.isBlank()){
            ValidationResult.Invalid("El mensaje no puede estar vacío")
        }else if (message.username.isBlank()){
            ValidationResult.Invalid("El nombre de usuario no puede estar vacío")
        } else {
            ValidationResult.Valid
        }
    }
}

fun ForumCreateDto.forumListValidation(): Boolean{
    var result = true
    this.listMessages.forEach {
        if(it.message.isEmpty() || it.username.isEmpty()){
            result  = false
        }
    }
    return result
}