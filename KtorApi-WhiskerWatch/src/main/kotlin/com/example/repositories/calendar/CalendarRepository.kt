package com.example.repositories.calendar

import com.example.models.calendar.Calendar
import com.example.repositories.ICrud
import java.util.*

interface CalendarRepository: ICrud<Calendar, UUID> {
    suspend fun findByMapsUuid(uuid: UUID): Calendar?
}