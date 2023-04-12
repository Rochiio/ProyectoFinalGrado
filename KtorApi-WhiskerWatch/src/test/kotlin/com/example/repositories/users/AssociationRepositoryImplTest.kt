package com.example.repositories.users

import com.example.models.users.Association
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AssociationRepositoryImplTest {
    private val repository = AssociationRepositoryImpl()
    private val test = Association(name = "test", email = "test@example.com", username = "test",
        password = "123456", description = "test description", url = "http://example.com")

    @Test
    @Order(1)
    fun save() = runTest {
        val created = repository.save(test)

        assertAll(
            { assertEquals(test.name, created.name) },
            { assertEquals(test.email, created.email) },
            { assertEquals(test.username, created.username) },
            { assertEquals(test.password, created.password) },
            { assertEquals(test.description, created.description) },
            { assertEquals(test.url, created.url) },
            { assertEquals(test.image, created.image) },
            { assertEquals(test.rol, created.rol) },
        )
    }

    @Test
    @Order(2)
    fun findByEmail() = runTest {
        val find = repository.findByEmail(test.email)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.name, find?.name) },
            { assertEquals(test.email, find?.email) },
            { assertEquals(test.username, find?.username) },
            { assertEquals(test.password, find?.password) },
            { assertEquals(test.description, find?.description) },
            { assertEquals(test.url, find?.url) },
            { assertEquals(test.image, find?.image) },
            { assertEquals(test.rol, find?.rol) },
        )
    }

    @Test
    @Order(3)
    fun findByUUID() = runTest {
        val find = repository.findByUUID(test.uuid)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.name, find?.name) },
            { assertEquals(test.email, find?.email) },
            { assertEquals(test.username, find?.username) },
            { assertEquals(test.password, find?.password) },
            { assertEquals(test.description, find?.description) },
            { assertEquals(test.url, find?.url) },
            { assertEquals(test.image, find?.image) },
            { assertEquals(test.rol, find?.rol) },
        )
    }

    @Test
    @Order(4)
    fun findAll() = runTest {
        val find = repository.findAll().toList()

        assertAll(
            { assertTrue(find.isNotEmpty()) },
            { assertEquals(test.name, find[0].name) },
            { assertEquals(test.email, find[0].email) },
            { assertEquals(test.username, find[0].username) },
            { assertEquals(test.password, find[0].password) },
            { assertEquals(test.description, find[0].description) },
            { assertEquals(test.url, find[0].url) },
            { assertEquals(test.image, find[0].image) },
            { assertEquals(test.rol, find[0].rol) },
        )
    }

    @Test
    @Order(5)
    fun update() = runTest {
        val update = test.copy(name = "update")
        val updated = repository.update(update)

        assertAll(
            { assertEquals(update.name, updated.name) },
            { assertEquals(update.email, updated.email) },
            { assertEquals(update.username, updated.username) },
            { assertEquals(update.password, updated.password) },
            { assertEquals(update.description, updated.description) },
            { assertEquals(update.url, updated.url) },
            { assertEquals(update.image, updated.image) },
            { assertEquals(update.rol, updated.rol) },
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