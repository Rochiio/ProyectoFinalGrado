package com.example.repositories.calendar

import com.example.models.calendar.Calendar
import com.example.repositories.ICrud

interface CalendarRepository: ICrud<Calendar, String> {
    suspend fun findByMapsUuid(uuid: String): Calendar?
}