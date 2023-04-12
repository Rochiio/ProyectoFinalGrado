package com.example.repositories.calendar

import com.example.models.calendar.Calendar
import com.example.models.calendar.Task
import com.example.models.forum.ForumMessages
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CalendarRepositoryImplTest {
    private val repository = CalendarRepositoryImpl()
    private val test = Calendar(mapsUUID = UUID.randomUUID().toString(),
        listTasks = mutableListOf(Task(date = LocalDate.now(), task = "tarea test")))

    @Test
    @Order(1)
    fun save() = runTest {
        val created = repository.save(test)

        assertAll(
            { assertEquals(test.mapsUUID, created.mapsUUID) },
            { assertEquals(test.listTasks, created.listTasks)}
        )
    }

    @Test
    @Order(2)
    fun findByMapsUuid() = runTest {
        val find = repository.findByMapsUuid(test.mapsUUID)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.mapsUUID, find?.mapsUUID) },
            { assertEquals(test.listTasks, find?.listTasks)}
        )
    }

    @Test
    @Order(3)
    fun findByUUID() = runTest {
        val find = repository.findByUUID(test.uuid)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.mapsUUID, find?.mapsUUID) },
            { assertEquals(test.listTasks, find?.listTasks)}
        )
    }

    @Test
    @Order(4)
    fun findAll() = runTest {
        val find = repository.findAll().toList()

        assertAll(
            { assertTrue(find.isNotEmpty()) },
            { assertEquals(test.mapsUUID, find[0].mapsUUID) },
            { assertEquals(test.listTasks, find[0].listTasks)}
        )
    }

    @Test
    @Order(5)
    fun update() = runTest {
        val lista = test.listTasks
        lista.add(Task(date = LocalDate.now(), task = "other task"))

        val update = test.copy(listTasks = lista)
        val updated = repository.update(update)

        assertAll(
            { assertEquals(update.mapsUUID, updated.mapsUUID) },
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