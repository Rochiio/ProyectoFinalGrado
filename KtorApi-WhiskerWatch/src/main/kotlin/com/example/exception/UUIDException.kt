package com.example.exception

sealed class UUIDException(message: String) : RuntimeException(message)
class UUIDBadRequestException(message: String) : UUIDException(message)