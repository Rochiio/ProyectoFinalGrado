package com.example.services.calendar


import com.example.dto.CalendarCreateDto
import com.example.exception.CalendarNotFoundException
import com.example.exception.MapsNotFoundException
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

    suspend fun findCalendarByMapsId(id: String): Calendario {
        return calendarRepository.findByMapsId(id)
            ?: throw MapsNotFoundException("No se ha encontrado un calendario con id de mapa $id")
    }

    suspend fun findCalendarById(id: String):Calendario{
        return calendarRepository.findById(id)
            ?: throw CalendarNotFoundException("No se ha encontrado un calendario con id $id")
    }

    suspend fun saveCalendar(calendar: CalendarCreateDto): Calendario{
        val created = Calendario(
            mapsId = calendar.mapsId,
            listTasks = calendar.listTasks.toListTasks().toMutableList()
        )
        return calendarRepository.save(created)
    }

    suspend fun updateCalendar(calendar: CalendarCreateDto, idCalendar: String): Calendario{
        val find = calendarRepository.findById(idCalendar)
        find?.let {
            val list = it.listTasks
            val newList = calendar.listTasks.toListTasks()
            val updated = Calendario(id = it.id, mapsId = calendar.mapsId,
                listTasks = (list + newList).toMutableList())
            return calendarRepository.update(updated)
        }?: run{
            throw CalendarNotFoundException("No se ha encontrado un calendario con id $idCalendar")
        }
    }

    suspend fun deleteCalendar(id: String): Boolean{
        val find = calendarRepository.findById(id)
        find?.let {
            return calendarRepository.delete(it)
        }?: run{
            throw CalendarNotFoundException("No se ha encontrado un calendario con id $id")
        }
    }

    suspend fun findAllCalendars(): List<Calendar>{
        return calendarRepository.findAll().toList()
    }

    suspend fun deleteAllCalendars(): Boolean{
        return calendarRepository.deleteAll()
    }
}
