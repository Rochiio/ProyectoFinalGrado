package com.example.error

sealed class CalendarError(val message: String){
    class CalendarNotFoundError(message: String): CalendarError(message)
    class CalendarBadRequestError(message: String): CalendarError(message)
}
