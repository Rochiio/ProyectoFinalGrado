package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class MapsCreateDto(
    var latitude: String,
    var longitude: String
)