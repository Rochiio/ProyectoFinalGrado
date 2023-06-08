package com.example.validators

import com.example.dto.CalendarCreateDto
import com.example.dto.TaskCreateDto
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.calendarValidation(){
    validate<CalendarCreateDto> { calendar ->
        if (calendar.mapsId.isBlank()){
            ValidationResult.Invalid("El id del mapa no puede estar vacío")
        }else{
            ValidationResult.Valid
        }
    }

    validate<TaskCreateDto> { task ->
        if (task.task.isBlank()){
            ValidationResult.Invalid("La tarea no puede estar vacía")
        }else if (task.date.isBlank()){
            ValidationResult.Invalid("La fecha de la tarea no puede estar vacía")
        }else if(!task.date.matches(Regex("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])\$"))){
            ValidationResult.Invalid("El formato de la fecha es incorrecto YYYY-MM-DD")
        }else {
            ValidationResult.Valid
        }
    }
}

fun CalendarCreateDto.taskListValidation(): Boolean{
    var correct = true
    this.listTasks.forEach {
        if(it.task.isEmpty() || it.date.isEmpty() ||
            !it.date.matches(Regex("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])\$"))){
            correct = false
        }
    }
    return correct
}