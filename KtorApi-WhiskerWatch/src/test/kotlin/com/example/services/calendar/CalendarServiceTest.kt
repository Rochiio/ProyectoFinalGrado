package com.example.services.calendar

import com.example.dto.CalendarCreateDto
import com.example.dto.TaskCreateDto
import com.example.exception.CalendarNotFoundException
import com.example.exception.MapsNotFoundException
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

    private val test = Calendar(mapsUUID = UUID.randomUUID().toString(),
        listTasks = mutableListOf(Task(date = LocalDate.now(), task = "tarea test")))
    private val createTest = CalendarCreateDto(mapsUUID = UUID.randomUUID().toString(),
        listTasks = mutableListOf(TaskCreateDto(date = LocalDate.now().toString(), task = "tarea test")))

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findCalendarByMapsUuid() = runTest{
        coEvery { repository.findByMapsUuid(test.mapsUUID) } returns test

        val find = service.findCalendarByMapsUuid(test.mapsUUID)
        assertAll(
            { assertEquals(test.id, find.id) },
            { assertEquals(test.uuid, find.uuid) },
            { assertEquals(test.mapsUUID, find.mapsUUID) },
            { assertEquals(test.listTasks, find.listTasks) },
        )

        coVerify(exactly = 1) {repository.findByMapsUuid(test.mapsUUID)}
    }

    @Test
    fun findCalendarByMapsUuidNotFound() = runTest {
        coEvery { repository.findByMapsUuid(test.mapsUUID) } returns null

        val exception = assertThrows<MapsNotFoundException> { service.findCalendarByMapsUuid(test.mapsUUID) }
        assertEquals("No se ha encontrado un calendario con uuid de mapa ${test.mapsUUID}", exception.message)

        coVerify(exactly = 1){repository.findByMapsUuid(test.mapsUUID)}
    }

    @Test
    fun findCalendarByUuid() = runTest{
        coEvery { repository.findByUUID(test.uuid) } returns test

        val find = service.findCalendarByUuid(test.uuid)
        assertAll(
            { assertEquals(test.id, find.id) },
            { assertEquals(test.uuid, find.uuid) },
            { assertEquals(test.mapsUUID, find.mapsUUID) },
            { assertEquals(test.listTasks, find.listTasks) },
        )

        coVerify(exactly = 1) {repository.findByUUID(test.uuid)}
    }

    @Test
    fun findCalendarByUuidNotFound() = runTest {
        coEvery { repository.findByUUID(test.uuid) } returns null

        val exception = assertThrows<CalendarNotFoundException> { service.findCalendarByUuid(test.uuid) }
        assertEquals("No se ha encontrado un calendario con uuid ${test.uuid}", exception.message)

        coVerify(exactly = 1){repository.findByUUID(test.uuid)}
    }

    @Test
    fun saveCalendar() = runTest{
        coEvery { repository.save(any()) } returns test

        val created = service.saveCalendar(createTest)
        assertAll(
            { assertEquals(test.id, created.id) },
            { assertEquals(test.uuid, created.uuid) },
            { assertEquals(test.mapsUUID, created.mapsUUID) },
            { assertEquals(test.listTasks, created.listTasks) },
        )

        coVerify(exactly = 1) {repository.save(any())}
    }

    @Test
    fun updateCalendar() = runTest{
        coEvery { repository.findByUUID(test.uuid) } returns test
        coEvery { repository.update(any()) } returns test

        val updated = service.updateCalendar(createTest, test.uuid)
        assertAll(
            { assertEquals(test.id, updated.id) },
            { assertEquals(test.uuid, updated.uuid) },
            { assertEquals(test.mapsUUID, updated.mapsUUID) },
            { assertEquals(test.listTasks, updated.listTasks) },
        )

        coVerify(exactly = 1) {repository.update(any())}
        coVerify(exactly = 1) {repository.findByUUID(test.uuid)}
    }

    @Test
    fun updateCalendarNotFound() = runTest {
        coEvery { repository.findByUUID(test.uuid) } returns null

        val exception = assertThrows<CalendarNotFoundException> { service.updateCalendar(createTest, test.uuid) }
        assertEquals("No se ha encontrado un calendario con uuid ${test.uuid}", exception.message)

        coVerify(exactly = 1) {repository.findByUUID(test.uuid)}
    }

    @Test
    fun deleteCalendar() = runTest{
        coEvery { repository.findByUUID(test.uuid) } returns test
        coEvery { repository.delete(test) } returns true

        val deleted = service.deleteCalendar(test.uuid)
        assertTrue(deleted)

        coVerify(exactly = 1){repository.findByUUID(test.uuid)}
        coVerify(exactly = 1){repository.delete(test)}
    }

    @Test
    fun deleteCalendarNotFound() = runTest {
        coEvery { repository.findByUUID(test.uuid) } returns null

        val exception = assertThrows<CalendarNotFoundException> { service.deleteCalendar(test.uuid) }
        assertEquals("No se ha encontrado un calendario con uuid ${test.uuid}", exception.message)

        coVerify(exactly = 1){repository.findByUUID(test.uuid)}
    }

    @Test
    fun findAllCalendars() = runTest{
        coEvery { repository.findAll() } returns flowOf(test)

        val find = service.findAllCalendars()
        assertAll(
            { assertTrue(find.isNotEmpty()) },
            { assertEquals(test.id, find[0].id) },
            { assertEquals(test.uuid, find[0].uuid) },
            { assertEquals(test.mapsUUID, find[0].mapsUUID) },
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