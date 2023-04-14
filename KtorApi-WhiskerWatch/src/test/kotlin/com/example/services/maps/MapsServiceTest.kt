package com.example.services.maps

import com.example.dto.MapsCreateDto
import com.example.exception.MapsNotFoundException
import com.example.models.Maps
import com.example.repositories.maps.MapRepositoryImpl
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
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class MapsServiceTest {
    @MockK
    private lateinit var repository: MapRepositoryImpl

    @InjectMockKs
    private lateinit var service: MapsService

    private var test = Maps(latitude = "1235.2", longitude = "12.54")
    private var createTest = MapsCreateDto(latitude = "1235.2", longitude = "12.54")

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findMapById() = runTest {
        coEvery { repository.findById(test.id) } returns test

        val find = service.findMapById(test.id)
        assertAll(
            { assertEquals(test.id, find.id) },
            { assertEquals(test.latitude, find.latitude) },
            { assertEquals(test.longitude, find.longitude) }
        )

        coVerify(exactly=1) { repository.findById(test.id)}
    }

    @Test
    fun findMayByUuidNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<MapsNotFoundException> { service.findMapById(test.id) }
        assertEquals("No se ha encontrado un mapa con el id ${test.id}", exception.message)

        coVerify(exactly=1) { repository.findById(test.id) }
    }

    @Test
    fun saveMap() = runTest {
        coEvery { repository.save(any()) } returns test

        val created = service.saveMap(createTest)
        assertAll(
            { assertEquals(test.id, created.id) },
            { assertEquals(test.latitude, created.latitude) },
            { assertEquals(test.longitude, created.longitude) }
        )

        coVerify(exactly=1) { repository.save(any()) }
    }

    @Test
    fun updateMap() = runTest {
        coEvery { repository.findById(test.id) } returns test
        coEvery { repository.update(test) } returns test

        val updated = service.updateMap(createTest, test.id)
        assertAll(
            { assertEquals(test.id, updated.id) },
            { assertEquals(test.id, updated.id) },
            { assertEquals(test.latitude, updated.latitude) },
            { assertEquals(test.longitude, updated.longitude) }
        )

        coVerify(exactly=1) { repository.findById(test.id) }
        coVerify(exactly=1) { repository.update(test) }
    }

    @Test
    fun updateMapNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<MapsNotFoundException> { service.updateMap(createTest, test.id) }
        assertEquals("No se ha encontrado un mapa con el id ${test.id}", exception.message)

        coVerify(exactly=1) { repository.findById(test.id) }
    }

    @Test
    fun deleteMap() = runTest {
        coEvery { repository.findById(test.id) } returns test
        coEvery { repository.delete(test) } returns true

        val deleted = service.deleteMap(test.id)
        assertTrue(deleted)

        coVerify(exactly = 1){repository.findById(test.id)}
        coVerify(exactly=1) {repository.delete(test)}
    }

    @Test
    fun deleteMapNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<MapsNotFoundException> { service.deleteMap(test.id) }
        assertEquals("No se ha encontrado un mapa con el id ${test.id}", exception.message)

        coVerify(exactly=1) { repository.findById(test.id)}
    }

    @Test
    fun findAllMaps() = runTest{
        coEvery { repository.findAll() } returns flowOf(test)

        val find = service.findAllMaps()
        assertAll(
            { assertTrue(find.isNotEmpty()) },
            { assertEquals(test.id, find[0].id) },
            { assertEquals(test.latitude, find[0].latitude) },
            { assertEquals(test.longitude, find[0].longitude) }
        )

        coVerify(exactly=1) { repository.findAll() }
    }

    @Test
    fun deleteAllMaps() = runTest{
        coEvery { repository.deleteAll() } returns true

        val deleted = service.deleteAllMaps()
        assertTrue(deleted)

        coVerify(exactly=1) { repository.deleteAll() }
    }
}