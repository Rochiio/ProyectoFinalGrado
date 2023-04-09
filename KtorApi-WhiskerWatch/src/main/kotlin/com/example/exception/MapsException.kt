package com.example.exception

sealed class MapsException(message: String) : RuntimeException(message)
class MapsNotFoundException(message: String) : MapsException(message)
class MapsBadRequestException(message: String) : MapsException(message)