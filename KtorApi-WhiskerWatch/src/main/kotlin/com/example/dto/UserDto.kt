package com.example.dto

import kotlinx.serialization.Serializable

/**
 * Clase DTO Serializable para la creacion de usuarios
 */
@Serializable
data class UserCreateDto(
    var name: String,
    var email: String,
    var password: String,
    var username: String,
    var rol: String
)

/**
 * Clase DTO Serializable para devolver los datos
 */
@Serializable
data class UserDto(
    var id: String,
    var name: String,
    var email: String,
    var username: String,
    var rol: String
)

/**
 * Clase DTO Serializable para devolver el usuario con su token
 */
@Serializable
data class UserWithTokenDto(
    var user: UserDto,
    var token: String
)

/**
 * Clase DTO Serializable para la realizacion del inicio de sesion
 */
@Serializable
data class UserLogin(
    var email: String,
    var password: String
)