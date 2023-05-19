package com.example.services.calendar

import com.example.dto.CalendarCreateDto
import com.example.dto.TaskCreateDto
import com.example.error.CalendarError
import com.example.error.MapsError
import com.example.models.Maps
import com.example.models.calendar.Calendar
import com.example.models.calendar.Task
import com.example.repositories.calendar.CalendarRepositoryImpl
import com.example.services.maps.MapsService
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.util.*

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class CalendarServiceTest {
    @MockK
    private lateinit var repository: CalendarRepositoryImpl

    @MockK
    private lateinit var mapsService: MapsService

    @InjectMockKs
    private lateinit var service: CalendarService

    private val test = Calendar(
        mapsId = UUID.randomUUID().toString(),
        listTasks = mutableListOf(Task(date = LocalDate.now().toString(), task = "tarea test"))
    )
    private val createTest = CalendarCreateDto(mapsId = UUID.randomUUID().toString(),
        listTasks = mutableListOf(TaskCreateDto(date = LocalDate.now().toString(), task = "tarea test")))

    private val map = Maps(latitude = "12.3", longitude = "1.23")

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findCalendarByMapsUuid() = runTest{
        coEvery { repository.findByMapsId(test.mapsId) } returns test

        val find = service.findCalendarByMapsId(test.mapsId)
        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.id, find.get()?.id) },
            { assertEquals(test.mapsId, find.get()?.mapsId) },
            { assertEquals(test.listTasks, find.get()?.listTasks) },
        )

        coVerify(exactly = 1) {repository.findByMapsId(test.mapsId)}
    }

    @Test
    fun findCalendarByMapsUuidNotFound() = runTest {
        coEvery { repository.findByMapsId(test.mapsId) } returns null

        val res = service.findCalendarByMapsId(test.mapsId)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is CalendarError.CalendarNotFoundError)},
            { assertEquals("No se ha encontrado un calendario con id de mapa ${test.mapsId}", res.getError()?.message) }
        )

        coVerify(exactly = 1){repository.findByMapsId(test.mapsId)}
    }

    @Test
    fun findCalendarByUuid() = runTest{
        coEvery { repository.findById(test.id) } returns test

        val find = service.findCalendarById(test.id)
        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.id, find.get()?.id) },
            { assertEquals(test.mapsId, find.get()?.mapsId) },
            { assertEquals(test.listTasks, find.get()?.listTasks) },
        )

        coVerify(exactly = 1) {repository.findById(test.id)}
    }

    @Test
    fun findCalendarByUuidNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val res = service.findCalendarById(test.id)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is CalendarError.CalendarNotFoundError)},
            { assertEquals("No se ha encontrado un calendario con id ${test.id}", res.getError()?.message) }
        )

        coVerify(exactly = 1){repository.findById(test.id)}
    }

    @Test
    fun saveCalendar() = runTest{
        coEvery { mapsService.findMapById(any()) } returns Ok(map)
        coEvery { repository.save(any()) } returns test

        val created = service.saveCalendar(createTest)
        assertAll(
            { assertNotNull(created) },
            { assertEquals(test.id, created.get()?.id) },
            { assertEquals(test.mapsId, created.get()?.mapsId) },
            { assertEquals(test.listTasks, created.get()?.listTasks) },
        )

        coVerify(exactly = 1) {repository.save(any())}
        coVerify(exactly=1) { mapsService.findMapById(any()) }
    }

    @Test
    fun saveCalendarMapsNotFound() = runTest {
        coEvery { mapsService.findMapById(any()) } returns Err(MapsError.MapsNotFoundError(""))

        val res = service.saveCalendar(createTest)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is CalendarError.CalendarNotFoundError)}
        )

        coVerify(exactly = 1){ mapsService.findMapById(any())}
    }

    @Test
    fun updateCalendar() = runTest{
        coEvery { mapsService.findMapById(any()) } returns Ok(map)
        coEvery { repository.findById(test.id) } returns test
        coEvery { repository.update(any()) } returns test

        val updated = service.updateCalendar(createTest, test.id)
        assertAll(
            { assertNotNull(updated) },
            { assertEquals(test.id, updated.get()?.id) },
            { assertEquals(test.mapsId, updated.get()?.mapsId) },
            { assertEquals(test.listTasks, updated.get()?.listTasks) },
        )

        coVerify(exactly = 1) {repository.update(any())}
        coVerify(exactly = 1) {repository.findById(test.id)}
        coVerify(exactly=1) {mapsService.findMapById(any())}
    }

    @Test
    fun updateCalendarNotFound() = runTest {
        coEvery { mapsService.findMapById(any()) } returns Ok(map)
        coEvery { repository.findById(test.id) } returns null

        val res = service.updateCalendar(createTest, test.id)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is CalendarError.CalendarNotFoundError)},
            { assertEquals("No se ha encontrado un calendario con id ${test.id}", res.getError()?.message) }
        )

        coVerify(exactly = 1) {repository.findById(test.id)}
        coVerify(exactly=1) {mapsService.findMapById(any())}
    }

    @Test
    fun updateCalendarMapsNotFound() = runTest {
        coEvery { mapsService.findMapById(any()) } returns Err(MapsError.MapsNotFoundError(""))

        val res = service.updateCalendar(createTest, test.id)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is CalendarError.CalendarNotFoundError)}
        )

        coVerify(exactly = 1){ mapsService.findMapById(any())}
    }

    @Test
    fun deleteCalendar() = runTest{
        coEvery { repository.findById(test.id) } returns test
        coEvery { repository.delete(test) } returns true

        val deleted = service.deleteCalendar(test.id)
        assertAll(
            { assertNotNull(deleted) },
            { assertTrue(deleted.get()!!) }
        )

        coVerify(exactly = 1){repository.findById(test.id)}
        coVerify(exactly = 1){repository.delete(test)}
    }

    @Test
    fun deleteCalendarNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val res = service.deleteCalendar(test.id)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is CalendarError.CalendarNotFoundError)},
            { assertEquals("No se ha encontrado un calendario con id ${test.id}", res.getError()?.message) }
        )

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