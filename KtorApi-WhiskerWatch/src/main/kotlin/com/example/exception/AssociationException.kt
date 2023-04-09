package com.example.exception

sealed class AssociationException(message: String): RuntimeException(message)
class AssociationNotFoundException(message: String): AssociationException(message)
class AssociationBadRequestException(message: String): AssociationException(message)