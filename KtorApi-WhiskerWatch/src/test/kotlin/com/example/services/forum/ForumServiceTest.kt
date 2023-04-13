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

    private val test = Forum(mapsUuid = UUID.randomUUID().toString(), listMessages =
    mutableListOf(ForumMessages(username = "pepe", message = "test")))

    private val createTest = ForumCreateDto(mapsUuid = UUID.randomUUID().toString(), listMessages =
    mutableListOf(ForumMessagesCreateDto(username = "pepe", message = "test")))

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findByMapsUuid() = runTest {
        coEvery { repository.findByMapsUuid(test.mapsUuid) } returns test

        val find = service.findByMapsUuid(test.mapsUuid)
        assertAll(
            { assertEquals(test.id, find.id) },
            { assertEquals(test.uuid, find.uuid) },
            { assertEquals(test.mapsUuid, find.mapsUuid) },
            { assertEquals(test.listMessages, find.listMessages) },
        )

        coVerify(exactly = 1) { repository.findByMapsUuid(test.mapsUuid)}
    }


    @Test
    fun findByMapsUuidNotFound() = runTest {
        coEvery { repository.findByMapsUuid(test.mapsUuid) } returns null

        val exception = assertThrows<MapsNotFoundException> { service.findByMapsUuid(test.mapsUuid) }
        assertEquals("No se ha encontrado un foro con uuid de mapa ${test.mapsUuid}", exception.message)

        coVerify(exactly=1)  { repository.findByMapsUuid(test.mapsUuid) }
    }

    @Test
    fun findByUuid() = runTest {
        coEvery { repository.findByUUID(test.uuid) } returns test

        val find = service.findByUuid(test.uuid)
        assertAll(
            { assertEquals(test.id, find.id) },
            { assertEquals(test.uuid, find.uuid) },
            { assertEquals(test.mapsUuid, find.mapsUuid) },
            { assertEquals(test.listMessages, find.listMessages) },
        )

        coVerify(exactly = 1) { repository.findByUUID(test.uuid)}
    }

    @Test
    fun findByUuidNotFound() = runTest {
        coEvery { repository.findByUUID(test.uuid) } returns null

        val exception = assertThrows<ForumNotFoundException> { service.findByUuid(test.uuid) }
        assertEquals("No se ha encontrado un foro con uuid ${test.uuid}", exception.message)

        coVerify(exactly=1)  { repository.findByUUID(test.uuid) }
    }

    @Test
    fun saveForum() = runTest{
        coEvery { repository.save(any()) } returns test

        val created = service.saveForum(createTest)
        assertAll(
            { assertEquals(test.id, created.id) },
            { assertEquals(test.uuid, created.uuid) },
            { assertEquals(test.mapsUuid, created.mapsUuid) },
            { assertEquals(test.listMessages, created.listMessages) },
        )

        coVerify(exactly=1) { repository.save(any())}
    }

    @Test
    fun updateForum() = runTest{
        coEvery { repository.findByUUID(test.uuid) } returns test
        coEvery { repository.update(any()) } returns test

        val updated = service.updateForum(createTest, test.uuid)
        assertAll(
            { assertEquals(test.id, updated.id) },
            { assertEquals(test.uuid, updated.uuid) },
            { assertEquals(test.mapsUuid, updated.mapsUuid) },
            { assertEquals(test.listMessages, updated.listMessages) },
        )

        coVerify(exactly=1) {repository.findByUUID(test.uuid)}
        coVerify(exactly=1) {repository.update(any())}
    }

    @Test
    fun updateForumNotFound() = runTest{
        coEvery { repository.findByUUID(test.uuid) } returns null

        val exception = assertThrows<ForumNotFoundException> { service.updateForum(createTest, test.uuid) }
        assertEquals("No se ha encontrado un foro con uuid ${test.uuid}", exception.message)

        coVerify(exactly=1) { repository.findByUUID(test.uuid)}
    }

    @Test
    fun deleteForum() = runTest{
        coEvery { repository.findByUUID(test.uuid)} returns test
        coEvery { repository.delete(test) } returns true

        val deleted = service.deleteForum(test.uuid)
        assertTrue(deleted)

        coVerify(exactly=1) {repository.findByUUID(test.uuid)}
        coVerify(exactly = 1) {repository.delete(test)}
    }

    @Test
    fun deleteForumNotFound() = runTest{
        coEvery { repository.findByUUID(test.uuid) } returns null

        val exception = assertThrows<ForumNotFoundException> { service.deleteForum(test.uuid) }
        assertEquals("No se ha encontrado un foro con uuid ${test.uuid}", exception.message)

        coVerify(exactly = 1) {repository.findByUUID(test.uuid)}
    }

    @Test
    fun findAllForums() = runTest{
        coEvery { repository.findAll() } returns flowOf(test)

        val find = service.findAllForums()
        assertAll(
            { assertTrue(find.isNotEmpty()) },
            { assertEquals(test.id, find[0].id) },
            { assertEquals(test.uuid, find[0].uuid) },
            { assertEquals(test.mapsUuid, find[0].mapsUuid) },
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