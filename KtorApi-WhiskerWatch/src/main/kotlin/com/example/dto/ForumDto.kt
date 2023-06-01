package com.example.dto

import kotlinx.serialization.Serializable

/**
 * Clase DTO Serializable para la creacion de foros
 */
@Serializable
data class ForumCreateDto(
    var mapsId: String,
    var listMessages: List<ForumMessagesCreateDto>
)

/**
 * Clase DTO Serializable para la creacion de mensajes
 */
@Serializable
data class ForumMessagesCreateDto(
    var username: String,
    var message: String,
    var created_At: String
)

