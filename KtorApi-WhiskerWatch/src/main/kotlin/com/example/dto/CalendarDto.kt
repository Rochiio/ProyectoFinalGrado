package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class CalendarCreateDto(
    var mapsUUID: String,
    var listTasksUuid: MutableList<String>
)


@Serializable
data class TaskCreateDto(
    var date: String,
    var task: String
)