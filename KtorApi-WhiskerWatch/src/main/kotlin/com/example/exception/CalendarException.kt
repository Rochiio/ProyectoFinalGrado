package com.example.exception

sealed class CalendarException(message: String): RuntimeException(message)
class CalendarNotFoundException(message: String): CalendarException(message)
class CalendarBadRequestException(message: String): CalendarException(message)