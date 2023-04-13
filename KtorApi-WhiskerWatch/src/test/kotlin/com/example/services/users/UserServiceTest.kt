package com.example.services.users

import com.example.dto.UserCreateDto
import com.example.exception.UserNotFoundException
import com.example.models.users.Rol
import com.example.models.users.User
import com.example.repositories.users.UserRepositoryImpl
import com.example.services.password.BcryptService
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
class UserServiceTest {
    @MockK
    private lateinit var repository: UserRepositoryImpl

    @MockK
    private lateinit var bcryptService: BcryptService

    @InjectMockKs
    private lateinit var service: UserService


    private var test = User(name = "test", email = "test", password = "test", username = "test")
    private var createTest = UserCreateDto(name = "test", email = "test", password = "test", username = "test", rol = Rol.USER.name)


    init {
        MockKAnnotations.init(this)
    }


    @Test
    fun findUserByEmail() = runTest {
        coEvery { repository.findByEmail(test.email) } returns test

        val find = service.findUserByEmail(test.email)
        assertAll(
            { assertEquals(test.id, find.id) },
            { assertEquals(test.uuid, find.uuid)},
            { assertEquals(test.name, find.name) },
            { assertEquals(test.email, find.email) },
            { assertEquals(test.password, find.password) },
            { assertEquals(test.username, find.username) },
            { assertEquals(test.rol, find.rol) }
        )

        coVerify(exactly = 1) { repository.findByEmail(test.email) }
    }

    @Test
    fun findUserByEmailNotFound() = runTest {
        coEvery { repository.findByEmail(test.email) } returns null

        val exception = assertThrows<UserNotFoundException> { service.findUserByEmail(test.email) }
        assertEquals("No se ha encontrado un usuario con email ${test.email}", exception.message)

        coVerify(exactly = 1) { repository.findByEmail(test.email) }
    }

    @Test
    fun findUserByUuid() = runTest{
        coEvery { repository.findByUUID(test.uuid) } returns test

        val find = service.findUserByUuid(test.uuid)
        assertAll(
            { assertEquals(test.id, find.id) },
            { assertEquals(test.uuid, find.uuid)},
            { assertEquals(test.name, find.name) },
            { assertEquals(test.email, find.email) },
            { assertEquals(test.password, find.password) },
            { assertEquals(test.username, find.username) },
            { assertEquals(test.rol, find.rol) }
        )

        coVerify(exactly = 1) { repository.findByUUID(test.uuid) }
    }

    @Test
    fun findUserByUuidNotFound() = runTest {
        coEvery { repository.findByUUID(test.uuid) } returns null

        val exception = assertThrows<UserNotFoundException> { service.findUserByUuid(test.uuid) }
        assertEquals("No se ha encontrado un usuario con uuid ${test.uuid}", exception.message)

        coVerify(exactly = 1) { repository.findByUUID(test.uuid) }
    }

    @Test
    fun saveUser() = runTest {
        coEvery { repository.save(any()) } returns test
        coEvery { bcryptService.encryptPassword(any()) } returns test.password

        val created = service.saveUser(createTest)
        assertAll(
            { assertEquals(test.id, created.id) },
            { assertEquals(test.uuid, created.uuid)},
            { assertEquals(test.name, created.name) },
            { assertEquals(test.email, created.email) },
            { assertEquals(test.password, created.password) },
            { assertEquals(test.username, created.username) },
            { assertEquals(test.rol, created.rol) }
        )

        coVerify(exactly = 1) { repository.save(any()) }
        coVerify(exactly = 1) { bcryptService.encryptPassword(any()) }
    }

    @Test
    fun updateUser() = runTest {
        coEvery { repository.findByUUID(test.uuid) } returns test
        coEvery { bcryptService.encryptPassword(any()) } returns test.password
        coEvery { repository.update(test) } returns test

        val updated = service.updateUser(createTest, test.uuid)
        assertAll(
            { assertEquals(test.id, updated.id) },
            { assertEquals(test.uuid, updated.uuid)},
            { assertEquals(test.name, updated.name) },
            { assertEquals(test.email, updated.email) },
            { assertEquals(test.password, updated.password) },
            { assertEquals(test.username, updated.username) },
            { assertEquals(test.rol, updated.rol) }
        )

        coVerify(exactly = 1) { repository.findByUUID(test.uuid) }
        coVerify(exactly = 1) { repository.update(test) }
        coVerify(exactly = 1) {bcryptService.encryptPassword(any())}
    }

    @Test
    fun updateUserNotFound() = runTest {
        coEvery { repository.findByUUID(test.uuid) } returns null

        val exception = assertThrows<UserNotFoundException> { service.updateUser(createTest, test.uuid) }
        assertEquals("No se ha encontrado un usuario con el UUID ${test.uuid}", exception.message)

        coVerify(exactly = 1) { repository.findByUUID(test.uuid) }
    }

    @Test
    fun deleteUser() = runTest {
        coEvery { repository.findByUUID(test.uuid) } returns test
        coEvery { repository.delete(any()) } returns true

        val deleted = service.deleteUser(test.uuid)
        assertTrue(deleted)

        coVerify(exactly = 1) { repository.findByUUID(test.uuid) }
        coVerify(exactly = 1) { repository.delete(any()) }
    }

    @Test
    fun deleteUserNotFound() = runTest {
        coEvery { repository.findByUUID(test.uuid) } returns null

        val exception = assertThrows<UserNotFoundException> { service.deleteUser(test.uuid) }
        assertEquals("No se ha encontrado un usario con el UUID ${test.uuid}", exception.message)

        coVerify(exactly = 1) { repository.findByUUID(test.uuid) }
    }

    @Test
    fun findAllUsers() = runTest {
        coEvery { repository.findAll() } returns flowOf(test)

        val find = service.findAllUsers()
        assertAll(
            { assertTrue(find.isNotEmpty()) },
            { assertEquals(test.id, find[0].id) },
            { assertEquals(test.uuid, find[0].uuid)},
            { assertEquals(test.name, find[0].name) },
            { assertEquals(test.email, find[0].email) },
            { assertEquals(test.password, find[0].password) },
            { assertEquals(test.username, find[0].username) },
            { assertEquals(test.rol, find[0].rol) }
        )

        coVerify(exactly = 1) { repository.findAll() }
    }

    @Test
    fun deleteAllUsers() = runTest {
        coEvery { repository.deleteAll() } returns true

        val deleted = service.deleteAllUsers()
        assertTrue(deleted)

        coVerify(exactly = 1) { repository.deleteAll() }
    }
}