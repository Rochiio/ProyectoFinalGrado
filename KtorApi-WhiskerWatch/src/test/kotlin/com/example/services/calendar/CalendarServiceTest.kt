package com.example.services.calendar

import com.example.dto.CalendarCreateDto
import com.example.dto.TaskCreateDto
import com.example.error.CalendarNotFoundException
import com.example.error.MapsNotFoundException
import com.example.models.calendar.Calendar
import com.example.models.calendar.Task
import com.example.repositories.calendar.CalendarRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.util.*

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class CalendarServiceTest {
    @MockK
    private lateinit var repository: CalendarRepositoryImpl

    @InjectMockKs
    private lateinit var service: CalendarService

    private val test = Calendar(
        mapsId = UUID.randomUUID().toString(),
        listTasks = mutableListOf(Task(date = LocalDate.now(), task = "tarea test"))
    )
    private val createTest = CalendarCreateDto(mapsId = UUID.randomUUID().toString(),
        listTasks = mutableListOf(TaskCreateDto(date = LocalDate.now().toString(), task = "tarea test")))

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findCalendarByMapsUuid() = runTest{
        coEvery { repository.findByMapsId(test.mapsId) } returns test

        val find = service.findCalendarByMapsId(test.mapsId)
        assertAll(
            { assertEquals(test.id, find.id) },
            { assertEquals(test.mapsId, find.mapsId) },
            { assertEquals(test.listTasks, find.listTasks) },
        )

        coVerify(exactly = 1) {repository.findByMapsId(test.mapsId)}
    }

    @Test
    fun findCalendarByMapsUuidNotFound() = runTest {
        coEvery { repository.findByMapsId(test.mapsId) } returns null

        val exception = assertThrows<MapsNotFoundException> { service.findCalendarByMapsId(test.mapsId) }
        assertEquals("No se ha encontrado un calendario con id de mapa ${test.mapsId}", exception.message)

        coVerify(exactly = 1){repository.findByMapsId(test.mapsId)}
    }

    @Test
    fun findCalendarByUuid() = runTest{
        coEvery { repository.findById(test.id) } returns test

        val find = service.findCalendarById(test.id)
        assertAll(
            { assertEquals(test.id, find.id) },
            { assertEquals(test.mapsId, find.mapsId) },
            { assertEquals(test.listTasks, find.listTasks) },
        )

        coVerify(exactly = 1) {repository.findById(test.id)}
    }

    @Test
    fun findCalendarByUuidNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<CalendarNotFoundException> { service.findCalendarById(test.id) }
        assertEquals("No se ha encontrado un calendario con id ${test.id}", exception.message)

        coVerify(exactly = 1){repository.findById(test.id)}
    }

    @Test
    fun saveCalendar() = runTest{
        coEvery { repository.save(any()) } returns test

        val created = service.saveCalendar(createTest)
        assertAll(
            { assertEquals(test.id, created.id) },
            { assertEquals(test.mapsId, created.mapsId) },
            { assertEquals(test.listTasks, created.listTasks) },
        )

        coVerify(exactly = 1) {repository.save(any())}
    }

    @Test
    fun updateCalendar() = runTest{
        coEvery { repository.findById(test.id) } returns test
        coEvery { repository.update(any()) } returns test

        val updated = service.updateCalendar(createTest, test.id)
        assertAll(
            { assertEquals(test.id, updated.id) },
            { assertEquals(test.mapsId, updated.mapsId) },
            { assertEquals(test.listTasks, updated.listTasks) },
        )

        coVerify(exactly = 1) {repository.update(any())}
        coVerify(exactly = 1) {repository.findById(test.id)}
    }

    @Test
    fun updateCalendarNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<CalendarNotFoundException> { service.updateCalendar(createTest, test.id) }
        assertEquals("No se ha encontrado un calendario con id ${test.id}", exception.message)

        coVerify(exactly = 1) {repository.findById(test.id)}
    }

    @Test
    fun deleteCalendar() = runTest{
        coEvery { repository.findById(test.id) } returns test
        coEvery { repository.delete(test) } returns true

        val deleted = service.deleteCalendar(test.id)
        assertTrue(deleted)

        coVerify(exactly = 1){repository.findById(test.id)}
        coVerify(exactly = 1){repository.delete(test)}
    }

    @Test
    fun deleteCalendarNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<CalendarNotFoundException> { service.deleteCalendar(test.id) }
        assertEquals("No se ha encontrado un calendario con id ${test.id}", exception.message)

        coVerify(exactly = 1){repository.findById(test.id)}
    }

    @Test
    fun findAllCalendars() = runTest{
        coEvery { repository.findAll() } returns flowOf(test)

        val find = service.findAllCalendars()
        assertAll(
            { assertTrue(find.isNotEmpty()) },
            { assertEquals(test.id, find[0].id) },
            { assertEquals(test.mapsId, find[0].mapsId) },
            { assertEquals(test.listTasks, find[0].listTasks) },
        )

        coVerify(exactly = 1){repository.findAll()}
    }

    @Test
    fun deleteAllCalendars() = runTest{
        coEvery { repository.deleteAll() } returns true

        val deleted = service.deleteAllCalendars()
        assertTrue(deleted)

        coVerify(exactly = 1){repository.deleteAll()}
    }
}