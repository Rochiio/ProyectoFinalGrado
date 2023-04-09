package com.example.utils

import com.example.exception.LocalDateBadRequestException
import java.time.LocalDate

fun String.toLocalDate(): LocalDate{
    return try{
        LocalDate.parse(this)
    }catch (e: Exception){
        throw LocalDateBadRequestException("Problemas para pasar la fecha")
    }
}