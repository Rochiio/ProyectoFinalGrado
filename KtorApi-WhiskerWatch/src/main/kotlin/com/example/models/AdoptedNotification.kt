package com.example.models


import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class AdoptedNotification(
    val map: Maps,
    val createdAt: String = LocalDateTime.now().toString()
)