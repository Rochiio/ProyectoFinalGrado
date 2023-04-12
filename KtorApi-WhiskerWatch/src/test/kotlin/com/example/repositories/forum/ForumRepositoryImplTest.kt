package com.example.repositories.forum

import com.example.models.forum.Forum
import com.example.models.forum.ForumMessages
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ForumRepositoryImplTest {
    private val repository = ForumRepositoryImpl()
    private val test = Forum(mapsUuid = UUID.randomUUID().toString(), listMessages =
    mutableListOf(ForumMessages(username = "pepe", message = "test")))

    @Test
    @Order(1)
    fun save() = runTest {
        val created = repository.save(test)

        assertAll(
            { assertEquals(test.mapsUuid, created.mapsUuid) },
            { assertEquals(test.listMessages, created.listMessages)}
        )
    }

    @Test
    @Order(2)
    fun findByMapsUuid() = runTest {
        val find = repository.findByMapsUuid(test.mapsUuid)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.mapsUuid, find?.mapsUuid) },
            { assertEquals(test.listMessages, find?.listMessages)}
        )
    }

    @Test
    @Order(3)
    fun findByUUID() = runTest {
        val find = repository.findByUUID(test.uuid)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.mapsUuid, find?.mapsUuid) },
            { assertEquals(test.listMessages, find?.listMessages)}
        )
    }

    @Test
    @Order(4)
    fun findAll() = runTest {
        val find = repository.findAll().toList()

        assertAll(
            { assertTrue(find.isNotEmpty()) },
            { assertEquals(test.mapsUuid, find[0].mapsUuid) },
            { assertEquals(test.listMessages, find[0].listMessages)}
        )
    }

    @Test
    @Order(5)
    fun update() = runTest{
        val lista = test.listMessages
        lista.add(ForumMessages(username = "nuevo", message = "test"))

        val update = test.copy(listMessages = lista)
        val updated = repository.update(update)

        assertAll(
            { assertEquals(update.mapsUuid, updated.mapsUuid) },
            { assertEquals(update.listMessages, updated.listMessages)}
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