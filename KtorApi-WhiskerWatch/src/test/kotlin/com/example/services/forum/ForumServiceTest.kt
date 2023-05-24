package com.example.services.forum

import com.example.dto.ForumCreateDto
import com.example.dto.ForumMessagesCreateDto
import com.example.error.ForumError
import com.example.error.MapsError
import com.example.models.Maps
import com.example.models.forum.Forum
import com.example.models.forum.ForumMessages
import com.example.repositories.forum.ForumRepositoryImpl
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
import java.util.*

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class ForumServiceTest {
    @MockK
    private lateinit var repository: ForumRepositoryImpl

    @MockK
    private lateinit var mapsService: MapsService

    @InjectMockKs
    private lateinit var service: ForumService

    private val test = Forum(
        mapsId = UUID.randomUUID().toString(), listMessages =
        mutableListOf(ForumMessages(username = "pepe", message = "test"))
    )

    private val createTest = ForumCreateDto(mapsId = UUID.randomUUID().toString(), listMessages =
    mutableListOf(ForumMessagesCreateDto(username = "pepe", message = "test", created_At = "2023-05-15")))

    private val map = Maps(latitude = "12.3", longitude = "1.23")

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findByMapsId() = runTest {
        coEvery { repository.findByMapsId(test.mapsId) } returns test

        val find = service.findByMapsId(test.mapsId)
        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.id, find.get()?.id) },
            { assertEquals(test.mapsId, find.get()?.mapsId) },
            { assertEquals(test.listMessages, find.get()?.listMessages) },
        )

        coVerify(exactly = 1) { repository.findByMapsId(test.mapsId)}
    }


    @Test
    fun findByMapsUuidNotFound() = runTest {
        coEvery { repository.findByMapsId(test.mapsId) } returns null

        val res = service.findByMapsId(test.mapsId)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is ForumError.ForumNotFoundError)},
            { assertEquals("No se ha encontrado un foro con id de mapa ${test.mapsId}", res.getError()?.message) }
        )

        coVerify(exactly=1)  { repository.findByMapsId(test.mapsId) }
    }

    @Test
    fun findByUuid() = runTest {
        coEvery { repository.findById(test.id) } returns test

        val find = service.findById(test.id)
        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.id, find.get()?.id) },
            { assertEquals(test.mapsId, find.get()?.mapsId) },
            { assertEquals(test.listMessages, find.get()?.listMessages) },
        )

        coVerify(exactly = 1) { repository.findById(test.id)}
    }

    @Test
    fun findByUuidNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val res = service.findById(test.id)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is ForumError.ForumNotFoundError)},
            { assertEquals("No se ha encontrado un foro con id ${test.id}", res.getError()?.message) }
        )

        coVerify(exactly=1)  { repository.findById(test.id) }
    }

    @Test
    fun saveForum() = runTest{
        coEvery { mapsService.findMapById(any()) } returns Ok(map)
        coEvery { repository.save(any()) } returns test

        val created = service.saveForum(createTest)
        assertAll(
            { assertNotNull(created) },
            { assertEquals(test.id, created.get()?.id) },
            { assertEquals(test.mapsId, created.get()?.mapsId) },
            { assertEquals(test.listMessages, created.get()?.listMessages) },
        )

        coVerify(exactly=1) { repository.save(any())}
        coVerify(exactly=1) { mapsService.findMapById(any()) }
    }

    @Test
    fun saveForumMapsNotFound() = runTest {
        coEvery { mapsService.findMapById(any()) } returns Err(MapsError.MapsNotFoundError(""))

        val res = service.saveForum(createTest)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is ForumError.ForumNotFoundError)}
        )

        coVerify(exactly = 1){ mapsService.findMapById(any())}
    }

    @Test
    fun updateForum() = runTest{
        coEvery { mapsService.findMapById(any()) } returns Ok(map)
        coEvery { repository.findById(test.id) } returns test
        coEvery { repository.update(any()) } returns test

        val updated = service.updateForum(createTest, test.id)
        assertAll(
            { assertNotNull(updated) },
            { assertEquals(test.id, updated.get()?.id) },
            { assertEquals(test.mapsId, updated.get()?.mapsId) },
            { assertEquals(test.listMessages, updated.get()?.listMessages) },
        )

        coVerify(exactly=1) {repository.findById(test.id)}
        coVerify(exactly=1) {repository.update(any())}
        coVerify(exactly=1) {mapsService.findMapById(any())}
    }

    @Test
    fun updateForumNotFound() = runTest{
        coEvery { mapsService.findMapById(any()) } returns Ok(map)
        coEvery { repository.findById(test.id) } returns null

        val res = service.updateForum(createTest, test.id)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is ForumError.ForumNotFoundError)},
            { assertEquals("No se ha encontrado un foro con id ${test.id}", res.getError()?.message) }
        )

        coVerify(exactly=1) { repository.findById(test.id)}
        coVerify(exactly=1) { mapsService.findMapById(any())}
    }

    @Test
    fun updateForumMapsNotFound() = runTest {
        coEvery { mapsService.findMapById(any()) } returns Err(MapsError.MapsNotFoundError(""))

        val res = service.updateForum(createTest, test.id)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is ForumError.ForumNotFoundError)}
        )

        coVerify(exactly = 1){ mapsService.findMapById(any())}
    }

    @Test
    fun deleteForum() = runTest{
        coEvery { repository.findById(test.id)} returns test
        coEvery { repository.delete(test) } returns true

        val deleted = service.deleteForum(test.id)
        assertAll(
            { assertNotNull(deleted)},
            { assertTrue(deleted.get()!!) }
        )

        coVerify(exactly=1) {repository.findById(test.id)}
        coVerify(exactly = 1) {repository.delete(test)}
    }

    @Test
    fun deleteForumNotFound() = runTest{
        coEvery { repository.findById(test.id) } returns null

        val res = service.deleteForum(test.id)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is ForumError.ForumNotFoundError)},
            { assertEquals("No se ha encontrado un foro con id ${test.id}", res.getError()?.message) }
        )

        coVerify(exactly = 1) {repository.findById(test.id)}
    }

    @Test
    fun findAllForums() = runTest{
        coEvery { repository.findAll() } returns flowOf(test)

        val find = service.findAllForums()
        assertAll(
            { assertTrue(find.isNotEmpty()) },
            { assertEquals(test.id, find[0].id) },
            { assertEquals(test.mapsId, find[0].mapsId) },
            { assertEquals(test.listMessages, find[0].listMessages) },
        )

        coVerify(exactly = 1){repository.findAll()}
    }

    @Test
    fun deleteAllForums() = runTest{
        coEvery { repository.deleteAll()} returns true

        val delete = service.deleteAllForums()
        assertTrue(delete)

        coVerify(exactly = 1){repository.deleteAll()}
    }
}