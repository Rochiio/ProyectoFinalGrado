package com.example.repositories.maps

import com.example.models.Maps
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class MapRepositoryImplTest {
    private val repository = MapRepositoryImpl()
    private val test = Maps(latitude ="1250.0", longitude= "15.60")

    @Test
    @Order(1)
    fun save() = runTest {
        val created = repository.save(test)

        assertAll(
            { assertEquals(test.latitude, created.latitude) },
            { assertEquals(test.longitude, created.longitude)}
        )
    }

    @Test
    @Order(2)
    fun findById() = runTest {
        val find = repository.findById(test.id)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.latitude, find?.latitude) },
            { assertEquals(test.longitude, find?.longitude)}
        )
    }

    @Test
    @Order(3)
    fun findAll() = runTest {
        val find = repository.findAll().toList()

        assertAll(
            { assertTrue(find.isNotEmpty()) },
            { assertEquals(test.latitude, find[0].latitude) },
            { assertEquals(test.longitude, find[0].longitude)}
        )
    }

    @Test
    @Order(4)
    fun update() = runTest {
        val update = test.copy(latitude = "123")
        val updated = repository.update(update)

        assertAll(
            { assertEquals(update.latitude, updated.latitude) },
            { assertEquals(update.longitude, updated.longitude)}
        )
    }

    @Test
    @Order(5)
    fun delete() =runTest {
        val deleted = repository.delete(test)

        assertTrue(deleted)
    }

    @Test
    @Order(6)
    fun deleteAll() = runTest {
        val deleted = repository.deleteAll()

        assertTrue(deleted)
    }
}