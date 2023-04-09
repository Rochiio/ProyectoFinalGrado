package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserCreateDto(
    var name: String,
    var email: String,
    var password: String,
    var username: String,
    var rol: String
)