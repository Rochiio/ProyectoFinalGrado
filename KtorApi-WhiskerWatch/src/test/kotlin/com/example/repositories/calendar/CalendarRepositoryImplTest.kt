package com.example.repositories.calendar

import com.example.models.calendar.Calendar
import com.example.models.calendar.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import java.time.LocalDate
import java.util.*

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CalendarRepositoryImplTest {
    private val repository = CalendarRepositoryImpl()
    private val test = Calendar(
        mapsId = UUID.randomUUID().toString(),
        listTasks = mutableListOf(Task(date = LocalDate.now().toString(), task = "tarea test"))
    )

    @Test
    @Order(1)
    fun save() = runTest {
        val created = repository.save(test)

        assertAll(
            { assertEquals(test.mapsId, created.mapsId) },
            { assertEquals(test.listTasks, created.listTasks)}
        )
    }

    @Test
    @Order(2)
    fun findByMapsId() = runTest {
        val find = repository.findByMapsId(test.mapsId)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.mapsId, find?.mapsId) },
            { assertEquals(test.listTasks, find?.listTasks)}
        )
    }

    @Test
    @Order(3)
    fun findById() = runTest {
        val find = repository.findById(test.id)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.mapsId, find?.mapsId) },
            { assertEquals(test.listTasks, find?.listTasks)}
        )
    }

    @Test
    @Order(4)
    fun findAll() = runTest {
        val find = repository.findAll().toList()

        assertAll(
            { assertTrue(find.isNotEmpty()) },
            { assertEquals(test.mapsId, find[0].mapsId) },
            { assertEquals(test.listTasks, find[0].listTasks)}
        )
    }

    @Test
    @Order(5)
    fun update() = runTest {
        val lista = test.listTasks
        lista.add(Task(date = LocalDate.now().toString(), task = "other task"))

        val update = test.copy(listTasks = lista)
        val updated = repository.update(update)

        assertAll(
            { assertEquals(update.mapsId, updated.mapsId) },
            { assertEquals(update.listTasks, updated.listTasks) }
        )
    }

    @Test
    @Order(6)
    fun delete() = runTest {
        val deleted = repository.delete(test)

        assertTrue(deleted)
    }

    @Test
    @Order(7)
    fun deleteAll() = runTest {
        val deleted = repository.deleteAll()

        assertTrue(deleted)
    }
}