package com.example.services.calendar


import com.example.dto.CalendarCreateDto
import com.example.error.CalendarError
import com.example.mappers.toListTasks
import com.example.models.calendar.Calendar
import com.example.repositories.calendar.CalendarRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single
import com.example.models.calendar.Calendar as Calendario

@Single
class CalendarService(
    private val calendarRepository: CalendarRepository
) {

    suspend fun findCalendarByMapsId(id: String): Result<Calendar, CalendarError> {
        return calendarRepository.findByMapsId(id)?.let {
            Ok(it)
        }?: run{
            Err(CalendarError.CalendarNotFoundError("No se ha encontrado un calendario con id de mapa $id"))
        }
    }

    suspend fun findCalendarById(id: String):Result<Calendar, CalendarError>{
        return calendarRepository.findById(id)?.let {
            Ok(it)
        }?: run {
            Err(CalendarError.CalendarNotFoundError("No se ha encontrado un calendario con id $id"))
        }
    }

    suspend fun saveCalendar(calendar: CalendarCreateDto): Result<Calendar, CalendarError>{
        val created = Calendario(
            mapsId = calendar.mapsId,
            listTasks = calendar.listTasks.toListTasks().toMutableList()
        )
        return Ok(calendarRepository.save(created))
    }

    suspend fun updateCalendar(calendar: CalendarCreateDto, idCalendar: String): Result<Calendar, CalendarError>{
        return calendarRepository.findById(idCalendar)?.let {
            val list = it.listTasks
            val newList = calendar.listTasks.toListTasks()
            val updated = Calendario(id = it.id, mapsId = calendar.mapsId,
                listTasks = (list + newList).toMutableList())
            Ok(calendarRepository.update(updated))
        }?: run{
            Err(CalendarError.CalendarNotFoundError("No se ha encontrado un calendario con id $idCalendar"))
        }
    }

    suspend fun deleteCalendar(id: String): Result<Boolean, CalendarError>{
        return calendarRepository.findById(id)?.let {
            Ok(calendarRepository.delete(it))
        }?: run{
            Err(CalendarError.CalendarNotFoundError("No se ha encontrado un calendario con id $id"))
        }
    }

    suspend fun findAllCalendars(): List<Calendar>{
        return calendarRepository.findAll().toList()
    }

    suspend fun deleteAllCalendars(): Boolean{
        return calendarRepository.deleteAll()
    }
}
