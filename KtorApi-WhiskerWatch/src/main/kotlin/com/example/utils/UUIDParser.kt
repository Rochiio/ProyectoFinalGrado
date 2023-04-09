package com.example.utils

import com.example.exception.UUIDBadRequestException
import java.util.*

fun String.toUUID(): UUID {
    return try {
        UUID.fromString(this)
    } catch (e: Exception) {
        throw UUIDBadRequestException("Problemas para pasar a UUID")
    }
}