package com.example.services.users

import com.example.dto.UserCreateDto
import com.example.error.UserError
import com.example.models.users.Rol
import com.example.models.users.User
import com.example.repositories.users.UserRepositoryImpl
import com.example.services.password.BcryptService
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
    private var updateTest = User(name = "updateTest", email = "test", password = "test", username = "update")
    private var createTest = UserCreateDto(name = "test", email = "test", password = "test", username = "test", rol = Rol.USER.name)


    init {
        MockKAnnotations.init(this)
    }


    @Test
    fun findUserByEmail() = runTest {
        coEvery { repository.findByEmail(test.email) } returns test

        val find = service.findUserByEmail(test.email)
        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.id, find.get()?.id) },
            { assertEquals(test.name, find.get()?.name) },
            { assertEquals(test.email, find.get()?.email) },
            { assertEquals(test.password, find.get()?.password) },
            { assertEquals(test.username, find.get()?.username) },
            { assertEquals(test.rol, find.get()?.rol) }
        )

        coVerify(exactly = 1) { repository.findByEmail(test.email) }
    }

    @Test
    fun findUserByEmailNotFound() = runTest {
        coEvery { repository.findByEmail(test.email) } returns null

        val res = service.findUserByEmail(test.email)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is UserError.UserNotFoundError)},
            { assertEquals("No se ha encontrado un usuario con email ${test.email}", res.getError()?.message) }
        )

        coVerify(exactly = 1) { repository.findByEmail(test.email) }
    }

    @Test
    fun findUserById() = runTest{
        coEvery { repository.findById(test.id) } returns test

        val find = service.findUserById(test.id)
        assertAll(
            { assertNotNull(find) },
            { assertEquals(test.id, find.get()?.id) },
            { assertEquals(test.name, find.get()?.name) },
            { assertEquals(test.email, find.get()?.email) },
            { assertEquals(test.password, find.get()?.password) },
            { assertEquals(test.username, find.get()?.username) },
            { assertEquals(test.rol, find.get()?.rol) }
        )

        coVerify(exactly = 1) { repository.findById(test.id) }
    }

    @Test
    fun findUserByIdNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val res = service.findUserById(test.id)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is UserError.UserNotFoundError)},
            { assertEquals("No se ha encontrado un usuario con id ${test.id}", res.getError()?.message) }
        )

        coVerify(exactly = 1) { repository.findById(test.id) }
    }

    @Test
    fun saveUser() = runTest {
        coEvery { repository.save(any()) } returns test
        coEvery { repository.findByEmail(any()) } returns null
        coEvery { bcryptService.encryptPassword(any()) } returns test.password

        val created = service.saveUser(createTest)
        assertAll(
            { assertNotNull(created) },
            { assertEquals(test.id, created.get()?.id) },
            { assertEquals(test.name, created.get()?.name) },
            { assertEquals(test.email, created.get()?.email) },
            { assertEquals(test.password, created.get()?.password) },
            { assertEquals(test.username, created.get()?.username) },
            { assertEquals(test.rol, created.get()?.rol) }
        )

        coVerify(exactly = 1) { repository.save(any()) }
        coVerify(exactly = 1) { repository.findByEmail(any()) }
        coVerify(exactly = 1) { bcryptService.encryptPassword(any()) }
    }

    @Test
    fun saveUserExists() = runTest {
        coEvery { repository.findByEmail(any()) } returns test

        val res = service.saveUser(createTest)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is UserError.UserBadRequestError)},
            { assertEquals("Ya existe un usuario con email ${test.email}", res.getError()?.message) }
        )

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
            { assertNotNull(updated) },
            { assertEquals(test.id, updated.get()?.id) },
            { assertEquals(test.name, updated.get()?.name) },
            { assertEquals(test.email, updated.get()?.email) },
            { assertEquals(test.password, updated.get()?.password) },
            { assertEquals(test.username, updated.get()?.username) },
            { assertEquals(test.rol, updated.get()?.rol) }
        )

        coVerify(exactly = 1) { repository.findByEmail(any())}
        coVerify(exactly = 1) { repository.findById(test.id) }
        coVerify(exactly = 1) { repository.update(test) }
        coVerify(exactly = 1) {bcryptService.encryptPassword(any())}
    }

    @Test
    fun updateUserNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val res = service.updateUser(createTest, test.id)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is UserError.UserNotFoundError)},
            { assertEquals("No se ha encontrado un usuario con el ID ${test.id}", res.getError()?.message) }
        )

        coVerify(exactly = 1) { repository.findById(test.id) }
    }

    @Test
    fun updateUserExists() = runTest {
        coEvery { repository.findByEmail(any()) } returns updateTest
        coEvery { repository.findById(test.id) } returns test

        val res = service.updateUser(createTest, test.id)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is UserError.UserBadRequestError)},
            { assertEquals("Ya existe un usuario con email ${test.email}", res.getError()?.message) }
        )

        coVerify(exactly = 1) { repository.findByEmail(any())}
        coVerify(exactly = 1) { repository.findById(test.id)}
    }

    @Test
    fun deleteUser() = runTest {
        coEvery { repository.findById(test.id) } returns test
        coEvery { repository.delete(any()) } returns true

        val deleted = service.deleteUser(test.id)
        assertAll(
            { assertNotNull(deleted) },
            { assertTrue(deleted.get()!!) }
        )

        coVerify(exactly = 1) { repository.findById(test.id) }
        coVerify(exactly = 1) { repository.delete(any()) }
    }

    @Test
    fun deleteUserNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val res = service.deleteUser(test.id)
        assertAll(
            { assertNotNull(res) },
            { assertTrue(res.get()==null) },
            { assertTrue(res.getError() != null) },
            { assertTrue(res.getError() is UserError.UserNotFoundError)},
            { assertEquals("No se ha encontrado un usario con el ID ${test.id}", res.getError()?.message) }
        )

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