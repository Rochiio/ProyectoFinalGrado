package com.example.repositories.users

import com.example.models.users.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*

import org.junit.jupiter.api.Assertions.*

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserRepositoryImplTest {
    private val userRepository = UserRepositoryImpl()
    private val userTest = User(name = "test", email = "test", password = "test", username = "test")


    @Test
    @Order(1)
    fun save() = runTest {
        val created = userRepository.save(userTest)

        assertAll(
            { assertEquals(userTest.name, created.name) },
            { assertEquals(userTest.email, created.email) },
            { assertEquals(userTest.password, created.password) },
            { assertEquals(userTest.username, created.username) },
            { assertEquals(userTest.rol, created.rol) }
            )
    }

    @Test
    @Order(2)
    fun findByEmail() = runTest {
        val find = userRepository.findByEmail(userTest.email)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(userTest.name, find?.name) },
            { assertEquals(userTest.email, find?.email) },
            { assertEquals(userTest.password, find?.password) },
            { assertEquals(userTest.username, find?.username) },
            { assertEquals(userTest.rol, find?.rol) }
        )
    }

    @Test
    @Order(3)
    fun findById() = runTest {
        val find = userRepository.findById(userTest.id)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(userTest.name, find?.name) },
            { assertEquals(userTest.email, find?.email) },
            { assertEquals(userTest.password, find?.password) },
            { assertEquals(userTest.username, find?.username) },
            { assertEquals(userTest.rol, find?.rol) }
        )
    }

    @Test
    @Order(4)
    fun findAll() = runTest {
        val list = userRepository.findAll().toList()
        assertAll(
            { assertTrue(list.isNotEmpty()) },
            { assertEquals(userTest.name, list[0].name) },
            { assertEquals(userTest.email, list[0].email) },
            { assertEquals(userTest.password, list[0].password) },
            { assertEquals(userTest.username, list[0].username) },
            { assertEquals(userTest.rol, list[0].rol) }
        )
    }

    @Test
    @Order(5)
    fun update() = runTest {
        val update = userTest.copy(name = "Otro")
        val updated = userRepository.update(update)

        assertAll(
            { assertEquals(update.name, updated.name) },
            { assertEquals(update.email, updated.email) },
            { assertEquals(update.password, updated.password) },
            { assertEquals(update.username, updated.username) },
            { assertEquals(update.rol, updated.rol) }
        )
    }

    @Test
    @Order(6)
    fun delete() = runTest {
        val deleted = userRepository.delete(userTest)

        assertTrue(deleted)
    }

    @Test
    @Order(7)
    fun deleteAll() = runTest {
        val deleted = userRepository.deleteAll()

        assertTrue(deleted)
    }
}