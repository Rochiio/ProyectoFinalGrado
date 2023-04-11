package com.example.services.calendar


import com.example.dto.CalendarCreateDto
import com.example.exception.CalendarBadRequestException
import com.example.exception.CalendarNotFoundException
import com.example.exception.UUIDBadRequestException
import com.example.mappers.toListTasks
import com.example.models.calendar.Calendar
import com.example.models.calendar.Calendar as Calendario
import com.example.repositories.calendar.CalendarRepository
import com.example.utils.toUUID
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single
import java.util.*

@Single
class CalendarService(
    private val calendarRepository: CalendarRepository
) {

    suspend fun findCalendarByMapsUuid(uuid: UUID): Calendario {
        return calendarRepository.findByMapsUuid(uuid)
            ?: throw CalendarNotFoundException("No se ha encontrado un calendario con uuid de mapa $uuid")
    }

    suspend fun findCalendarByUuid(uuid: UUID):Calendario{
        return calendarRepository.findByUUID(uuid)
            ?: throw CalendarNotFoundException("No se ha encontrado un calendario con uuid $uuid")
    }

    suspend fun saveCalendar(calendar: CalendarCreateDto): Calendario{
        try {
            val created = Calendario(
                mapsUUID = calendar.mapsUUID.toUUID(),
                listTasks = calendar.listTasks.toListTasks().toMutableList()
            )
            return calendarRepository.save(created)
        }catch (e: UUIDBadRequestException){
            throw CalendarBadRequestException(e.message.toString())
        }
    }

    suspend fun updateCalendar(calendar: CalendarCreateDto, uuidCalendar: UUID): Calendario{
        val find = calendarRepository.findByUUID(uuidCalendar)
        find?.let {
            try{
                val list = it.listTasks
                val newList = calendar.listTasks.toListTasks()
                val updated = Calendario(id = it.id, uuid = it.uuid, mapsUUID = calendar.mapsUUID.toUUID(),
                    listTasks = (list + newList).toMutableList())
                return calendarRepository.update(updated)
            }catch (e: UUIDBadRequestException){
                throw CalendarBadRequestException(e.message.toString())
            }
        }?: run{
            throw CalendarNotFoundException("No se ha encontrado un calendario con uuid $uuidCalendar")
        }
    }

    suspend fun deleteCalendar(uuid: UUID): Boolean{
        val find = calendarRepository.findByUUID(uuid)
        find?.let {
            return calendarRepository.delete(it)
        }?: run{
            throw CalendarNotFoundException("No se ha encontrado un calendario con uuid $uuid")
        }
    }

    suspend fun findAllCalendars(): List<Calendar>{
        return calendarRepository.findAll().toList()
    }

    suspend fun deleteAllCalendars(): Boolean{
        return calendarRepository.deleteAll()
    }
}
