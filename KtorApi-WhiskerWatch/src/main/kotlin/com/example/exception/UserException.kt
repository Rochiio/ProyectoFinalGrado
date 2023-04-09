package com.example.exception

sealed class UserException(message: String) : RuntimeException(message)
class UserNotFoundException(message: String) : UserException(message)
class UserBadRequestException(message: String) : UserException(message)