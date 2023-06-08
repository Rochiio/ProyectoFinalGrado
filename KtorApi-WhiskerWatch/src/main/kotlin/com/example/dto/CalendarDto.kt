package com.example.dto

import kotlinx.serialization.Serializable

/**
 * Clase DTO Serializable para la creacion de calendarios
 */
@Serializable
data class CalendarCreateDto(
    var mapsId: String,
    var listTasks: MutableList<TaskCreateDto>
)

/**
 * Clase DTO Serializable para la creacion de tareas
 */
@Serializable
data class TaskCreateDto(
    var date: String,
    var task: String
)