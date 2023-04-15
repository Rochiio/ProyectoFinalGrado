package com.example.error

sealed class ForumError(val message: String) {
    class ForumNotFoundError(message: String) : ForumError(message)
    class ForumBadRequestError(message: String) : ForumError(message)
}
