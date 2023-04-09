package com.example.validators

import com.example.dto.MapsCreateDto
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.mapsValidation(){
    validate<MapsCreateDto> { maps ->
        if (maps.latitude.isBlank()){
            ValidationResult.Invalid("La latitud no puede estar vacía")
        }else if (maps.longitude.isBlank()){
            ValidationResult.Invalid("La longitud no puede estar vacía")
        }else {
            ValidationResult.Valid
        }
    }
}