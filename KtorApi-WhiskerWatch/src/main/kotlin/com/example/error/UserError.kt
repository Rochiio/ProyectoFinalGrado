package com.example.error

sealed class UserError(val message: String){
    class UserNotFoundError(message: String) : UserError(message)
    class UserBadRequestError(message: String) : UserError(message)
}
