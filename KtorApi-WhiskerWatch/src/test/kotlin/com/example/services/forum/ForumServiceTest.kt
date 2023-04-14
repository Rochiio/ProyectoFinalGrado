package com.example.services.forum

import com.example.dto.ForumCreateDto
import com.example.dto.ForumMessagesCreateDto
import com.example.exception.ForumNotFoundException
import com.example.exception.MapsNotFoundException
import com.example.models.forum.Forum
import com.example.models.forum.ForumMessages
import com.example.repositories.forum.ForumRepositoryImpl
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
import java.util.*

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class ForumServiceTest {
    @MockK
    private lateinit var repository: ForumRepositoryImpl

    @InjectMockKs
    private lateinit var service: ForumService

    private val test = Forum(
        mapsId = UUID.randomUUID().toString(), listMessages =
        mutableListOf(ForumMessages(username = "pepe", message = "test"))
    )

    private val createTest = ForumCreateDto(mapsId = UUID.randomUUID().toString(), listMessages =
    mutableListOf(ForumMessagesCreateDto(username = "pepe", message = "test")))

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findByMapsId() = runTest {
        coEvery { repository.findByMapsId(test.mapsId) } returns test

        val find = service.findByMapsId(test.mapsId)
        assertAll(
            { assertEquals(test.id, find.id) },
            { assertEquals(test.mapsId, find.mapsId) },
            { assertEquals(test.listMessages, find.listMessages) },
        )

        coVerify(exactly = 1) { repository.findByMapsId(test.mapsId)}
    }


    @Test
    fun findByMapsUuidNotFound() = runTest {
        coEvery { repository.findByMapsId(test.mapsId) } returns null

        val exception = assertThrows<MapsNotFoundException> { service.findByMapsId(test.mapsId) }
        assertEquals("No se ha encontrado un foro con id de mapa ${test.mapsId}", exception.message)

        coVerify(exactly=1)  { repository.findByMapsId(test.mapsId) }
    }

    @Test
    fun findByUuid() = runTest {
        coEvery { repository.findById(test.id) } returns test

        val find = service.findById(test.id)
        assertAll(
            { assertEquals(test.id, find.id) },
            { assertEquals(test.mapsId, find.mapsId) },
            { assertEquals(test.listMessages, find.listMessages) },
        )

        coVerify(exactly = 1) { repository.findById(test.id)}
    }

    @Test
    fun findByUuidNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<ForumNotFoundException> { service.findById(test.id) }
        assertEquals("No se ha encontrado un foro con id ${test.id}", exception.message)

        coVerify(exactly=1)  { repository.findById(test.id) }
    }

    @Test
    fun saveForum() = runTest{
        coEvery { repository.save(any()) } returns test

        val created = service.saveForum(createTest)
        assertAll(
            { assertEquals(test.id, created.id) },
            { assertEquals(test.mapsId, created.mapsId) },
            { assertEquals(test.listMessages, created.listMessages) },
        )

        coVerify(exactly=1) { repository.save(any())}
    }

    @Test
    fun updateForum() = runTest{
        coEvery { repository.findById(test.id) } returns test
        coEvery { repository.update(any()) } returns test

        val updated = service.updateForum(createTest, test.id)
        assertAll(
            { assertEquals(test.id, updated.id) },
            { assertEquals(test.mapsId, updated.mapsId) },
            { assertEquals(test.listMessages, updated.listMessages) },
        )

        coVerify(exactly=1) {repository.findById(test.id)}
        coVerify(exactly=1) {repository.update(any())}
    }

    @Test
    fun updateForumNotFound() = runTest{
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<ForumNotFoundException> { service.updateForum(createTest, test.id) }
        assertEquals("No se ha encontrado un foro con id ${test.id}", exception.message)

        coVerify(exactly=1) { repository.findById(test.id)}
    }

    @Test
    fun deleteForum() = runTest{
        coEvery { repository.findById(test.id)} returns test
        coEvery { repository.delete(test) } returns true

        val deleted = service.deleteForum(test.id)
        assertTrue(deleted)

        coVerify(exactly=1) {repository.findById(test.id)}
        coVerify(exactly = 1) {repository.delete(test)}
    }

    @Test
    fun deleteForumNotFound() = runTest{
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<ForumNotFoundException> { service.deleteForum(test.id) }
        assertEquals("No se ha encontrado un foro con id ${test.id}", exception.message)

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