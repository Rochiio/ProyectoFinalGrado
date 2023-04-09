package com.example.validators

import com.example.dto.CalendarCreateDto
import com.example.dto.TaskCreateDto
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.calendarValidation(){
    validate<CalendarCreateDto> { calendar ->
        if (calendar.mapsUUID.isBlank()){
            ValidationResult.Invalid("El uuid del mapa no puede estar vacío")
        }else{
            ValidationResult.Valid
        }
    }

    validate<TaskCreateDto> { task ->
        if (task.task.isBlank()){
            ValidationResult.Invalid("La tarea no puede estar vacía")
        }else if (task.date.isBlank()){
            ValidationResult.Invalid("La fecha de la tarea no puede estar vacía")
        }else {
            ValidationResult.Valid
        }
    }
}