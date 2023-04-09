package com.example.exception

sealed class LocalDateException(message: String) : RuntimeException(message)
class LocalDateBadRequestException(message: String) : LocalDateException(message)