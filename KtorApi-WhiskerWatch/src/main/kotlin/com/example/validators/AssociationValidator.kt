package com.example.validators

import com.example.dto.AssociationCreateDto
import com.example.models.users.Rol
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.associationValidation(){
    validate<AssociationCreateDto> { association ->
        if(association.email.isBlank() || !association.email.matches(Regex("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$"))){
            ValidationResult.Invalid("El correo no puede estar vacío o es incorrecto")
        }else if(!Rol.values().map { it.name }.toList().contains(association.rol) || association.rol.isBlank()){
            ValidationResult.Invalid("El rol no puede estar vacío o es incorrecto")
        }else if (association.name.isBlank()){
            ValidationResult.Invalid("El nombre no puede estar vacío")
        }else if (association.description.isBlank()){
            ValidationResult.Invalid("La descripcion no puede estar vacío")
        }else if (association.url.isBlank()){
            ValidationResult.Invalid("La url no puede estar vacía")
        }else if (association.username.isBlank()){
            ValidationResult.Invalid("El nombre de usario no puede estar vacía")
        }else if (association.password.isBlank() || association.password.length < 6){
            ValidationResult.Invalid("La contraseña no puede ser vacía o tener una longitud menor de 6")
        }else {
            ValidationResult.Valid
        }
    }
}