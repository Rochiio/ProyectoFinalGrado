package com.example.dto

import kotlinx.serialization.Serializable

/**
 * Clase DTO Serializable para la creacion de mapas
 */
@Serializable
data class MapsCreateDto(
    var latitude: String,
    var longitude: String
)