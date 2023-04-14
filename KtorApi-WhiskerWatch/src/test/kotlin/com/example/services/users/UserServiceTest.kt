package com.example.services.users

import com.example.dto.UserCreateDto
import com.example.exception.UserBadRequestException
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
    fun findUserById() = runTest{
        coEvery { repository.findById(test.id) } returns test

        val find = service.findUserById(test.id)
        assertAll(
            { assertEquals(test.id, find.id) },
            { assertEquals(test.name, find.name) },
            { assertEquals(test.email, find.email) },
            { assertEquals(test.password, find.password) },
            { assertEquals(test.username, find.username) },
            { assertEquals(test.rol, find.rol) }
        )

        coVerify(exactly = 1) { repository.findById(test.id) }
    }

    @Test
    fun findUserByIdNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<UserNotFoundException> { service.findUserById(test.id) }
        assertEquals("No se ha encontrado un usuario con id ${test.id}", exception.message)

        coVerify(exactly = 1) { repository.findById(test.id) }
    }

    @Test
    fun saveUser() = runTest {
        coEvery { repository.save(any()) } returns test
        coEvery { repository.findByEmail(any()) } returns null
        coEvery { bcryptService.encryptPassword(any()) } returns test.password

        val created = service.saveUser(createTest)
        assertAll(
            { assertEquals(test.id, created.id) },
            { assertEquals(test.name, created.name) },
            { assertEquals(test.email, created.email) },
            { assertEquals(test.password, created.password) },
            { assertEquals(test.username, created.username) },
            { assertEquals(test.rol, created.rol) }
        )

        coVerify(exactly = 1) { repository.save(any()) }
        coVerify(exactly = 1) { repository.findByEmail(any()) }
        coVerify(exactly = 1) { bcryptService.encryptPassword(any()) }
    }

    @Test
    fun saveUserExists() = runTest {
        coEvery { repository.findByEmail(any()) } returns test

        val exception = assertThrows<UserBadRequestException> { service.saveUser(createTest) }
        assertEquals("Ya existe un usuario con email ${test.email}", exception.message)

        coVerify(exactly = 1) {repository.findByEmail(any())}
    }

    @Test
    fun updateUser() = runTest {
        coEvery { repository.findByEmail(any()) } returns null
        coEvery { repository.findById(test.id) } returns test
        coEvery { bcryptService.encryptPassword(any()) } returns test.password
        coEvery { repository.update(test) } returns test

        val updated = service.updateUser(createTest, test.id)
        assertAll(
            { assertEquals(test.id, updated.id) },
            { assertEquals(test.name, updated.name) },
            { assertEquals(test.email, updated.email) },
            { assertEquals(test.password, updated.password) },
            { assertEquals(test.username, updated.username) },
            { assertEquals(test.rol, updated.rol) }
        )

        coVerify(exactly = 1) { repository.findByEmail(any())}
        coVerify(exactly = 1) { repository.findById(test.id) }
        coVerify(exactly = 1) { repository.update(test) }
        coVerify(exactly = 1) {bcryptService.encryptPassword(any())}
    }

    @Test
    fun updateUserNotFound() = runTest {
        coEvery { repository.findByEmail(any()) } returns null
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<UserNotFoundException> { service.updateUser(createTest, test.id) }
        assertEquals("No se ha encontrado un usuario con el ID ${test.id}", exception.message)

        coVerify(exactly = 1) { repository.findByEmail(any())}
        coVerify(exactly = 1) { repository.findById(test.id) }
    }

    @Test
    fun updateUserExists() = runTest {
        coEvery { repository.findByEmail(any()) } returns test

        val exception = assertThrows<UserBadRequestException> { service.updateUser(createTest, test.id) }
        assertEquals("Ya existe un usuario con email ${test.email}", exception.message)

        coVerify(exactly = 1) { repository.findByEmail(any())}
    }

    @Test
    fun deleteUser() = runTest {
        coEvery { repository.findById(test.id) } returns test
        coEvery { repository.delete(any()) } returns true

        val deleted = service.deleteUser(test.id)
        assertTrue(deleted)

        coVerify(exactly = 1) { repository.findById(test.id) }
        coVerify(exactly = 1) { repository.delete(any()) }
    }

    @Test
    fun deleteUserNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<UserNotFoundException> { service.deleteUser(test.id) }
        assertEquals("No se ha encontrado un usario con el ID ${test.id}", exception.message)

        coVerify(exactly = 1) { repository.findById(test.id) }
    }

    @Test
    fun findAllUsers() = runTest {
        coEvery { repository.findAll() } returns flowOf(test)

        val find = service.findAllUsers()
        assertAll(
            { assertTrue(find.isNotEmpty()) },
            { assertEquals(test.id, find[0].id) },
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