package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserCreateDto(
    var name: String,
    var email: String,
    var password: String,
    var username: String,
    var rol: String,
    var isAdmin: Boolean = false
)


@Serializable
data class UserDto(
    var id: String,
    var name: String,
    var email: String,
    var username: String,
    var rol: String,
    var isAdmin: Boolean
)


@Serializable
data class UserWithTokenDto(
    var user: UserDto,
    var token: String
)


@Serializable
data class UserLogin(
    var email: String,
    var password: String
)