package com.example.error

sealed class AssociationError(val message: String) {
    class AssociationNotFoundError(message: String): AssociationError(message)
    class AssociationBadRequestError(message: String): AssociationError(message)
}
