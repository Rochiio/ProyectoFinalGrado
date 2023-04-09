package com.example.dto

import kotlinx.serialization.Serializable

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