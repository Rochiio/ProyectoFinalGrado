package com.example.services.calendar


import com.example.dto.CalendarCreateDto
import com.example.exception.CalendarBadRequestException
import com.example.exception.CalendarNotFoundException
import com.example.exception.MapsNotFoundException
import com.example.exception.UUIDBadRequestException
import com.example.mappers.toListTasks
import com.example.models.calendar.Calendar
import com.example.repositories.calendar.CalendarRepository
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single
import com.example.models.calendar.Calendar as Calendario

@Single
class CalendarService(
    private val calendarRepository: CalendarRepository
) {

    suspend fun findCalendarByMapsUuid(uuid: String): Calendario {
        return calendarRepository.findByMapsUuid(uuid)
            ?: throw MapsNotFoundException("No se ha encontrado un calendario con uuid de mapa $uuid")
    }

    suspend fun findCalendarByUuid(uuid: String):Calendario{
        return calendarRepository.findByUUID(uuid)
            ?: throw CalendarNotFoundException("No se ha encontrado un calendario con uuid $uuid")
    }

    suspend fun saveCalendar(calendar: CalendarCreateDto): Calendario{
        val created = Calendario(
            mapsUUID = calendar.mapsUUID,
            listTasks = calendar.listTasks.toListTasks().toMutableList()
        )
        return calendarRepository.save(created)
    }

    suspend fun updateCalendar(calendar: CalendarCreateDto, uuidCalendar: String): Calendario{
        val find = calendarRepository.findByUUID(uuidCalendar)
        find?.let {
            val list = it.listTasks
            val newList = calendar.listTasks.toListTasks()
            val updated = Calendario(id = it.id, uuid = it.uuid, mapsUUID = calendar.mapsUUID,
                listTasks = (list + newList).toMutableList())
            return calendarRepository.update(updated)
        }?: run{
            throw CalendarNotFoundException("No se ha encontrado un calendario con uuid $uuidCalendar")
        }
    }

    suspend fun deleteCalendar(uuid: String): Boolean{
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
