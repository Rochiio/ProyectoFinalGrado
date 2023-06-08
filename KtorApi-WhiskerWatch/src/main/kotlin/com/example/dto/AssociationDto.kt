package com.example.dto

import kotlinx.serialization.Serializable

/**
 * Clase DTO Serializable para la creacion de asociaciones
 */
@Serializable
data class AssociationCreateDto(
    var name: String,
    var email: String,
    var username: String,
    var password: String,
    var rol: String,
    var description: String,
    var url: String,
)

/**
 * Clase DTO Serializable para mostrar como respuesta
 */
@Serializable
data class AssociationDto(
    val id: String,
    var name: String,
    var email: String,
    var username: String,
    val rol: String,
    var description: String,
    var url: String,
    var image: String?,
)

/**
 * Clase DTO Serializable para devolver al con token
 */
@Serializable
data class  AssociationTokenDto(
    var association: AssociationDto,
    var token: String
)

/**
 * Clase DTO Serializable para hacer el inicio de sesion
 */
@Serializable
data class AssociationLogin(
    var email: String,
    var password: String
)