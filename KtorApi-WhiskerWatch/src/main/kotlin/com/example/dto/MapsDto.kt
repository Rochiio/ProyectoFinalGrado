package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class MapsCreateDto(
    var latitude: String,
    var longitude: String
)


@Serializable
data class MapsDto(
    var id: String,
    var latitude: String,
    var longitude: String
)