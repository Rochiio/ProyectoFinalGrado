package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class ForumCreateDto(
    var mapsId: String,
    var listMessages: List<ForumMessagesCreateDto>
)

@Serializable
data class ForumMessagesCreateDto(
    var username: String,
    var message: String,
)