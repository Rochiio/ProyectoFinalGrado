package com.example.exception

sealed class ForumException(message: String) : RuntimeException(message)
class ForumNotFoundException(message: String) : ForumException(message)
class ForumBadRequestException(message: String) : ForumException(message)