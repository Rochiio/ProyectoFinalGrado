package com.example.validators

import com.example.dto.UserCreateDto
import com.example.dto.UserLogin
import com.example.models.users.Rol
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.userValidation(){
    validate<UserCreateDto> { user ->
        if (user.email.isBlank() || !user.email.matches(Regex("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$"))){
            ValidationResult.Invalid("El email no puede estar vacío o es incorrecto")
        } else if (!Rol.values().map { it.name }.toList().contains(user.rol) || user.rol.isBlank()){
            ValidationResult.Invalid("El rol no puede estar vacío o es incorrecto")
        } else if (user.name.isBlank()){
            ValidationResult.Invalid("El nombre no puede estar vacío")
        } else if (user.password.isBlank() || user.password.length < 6){
            ValidationResult.Invalid("La contraseña no puede estar vacía o tener menos de 6 caracteres")
        } else if (user.username.isBlank()) {
            ValidationResult.Invalid("El nombre de usuario no puede estar vacío")
        }else {
            ValidationResult.Valid
        }
    }

    validate<UserLogin> { user ->
        if(user.email.isBlank() || !user.email.matches(Regex("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$"))){
            ValidationResult.Invalid("El correo no puede estar vacío o es incorrecto")
        }else if (user.password.isBlank()){
            ValidationResult.Invalid("La contraseña no puede estar vacía")
        }else{
            ValidationResult.Valid
        }
    }
}