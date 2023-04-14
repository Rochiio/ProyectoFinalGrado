package com.example.services.users

import com.example.dto.AssociationCreateDto
import com.example.exception.AssociationBadRequestException
import com.example.exception.AssociationNotFoundException
import com.example.models.users.Association
import com.example.models.users.Rol
import com.example.repositories.users.AssociationRepositoryImpl
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
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class AssociationServiceTest {
    @MockK
    private lateinit var repository: AssociationRepositoryImpl

    @MockK
    private lateinit var bcryptService: BcryptService

    @InjectMockKs
    private lateinit var service: AssociationService


    private var test = Association(
        name = "test", email = "test@example.com", username = "test",
        password = "123456", description = "test description", url = "http://example.com"
    )
    private var createTest = AssociationCreateDto(name = "test", email = "test@example.com", username = "test",
        password = "123456", description = "test description", url = "http://example.com", rol = Rol.ASSOCIATION.name)


    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun findAssociationByEmail() = runTest{
        coEvery { repository.findByEmail(test.email) } returns test

        val find = service.findAssociationByEmail(test.email)
        assertAll(
            { assertEquals(test.id, find.id) },
            { assertEquals(test.name, find.name) },
            { assertEquals(test.email, find.email) },
            { assertEquals(test.password, find.password) },
            { assertEquals(test.username, find.username) },
            { assertEquals(test.description, find.description) },
            { assertEquals(test.url, find.url) },
            { assertEquals(test.rol, find.rol) }
        )

        coVerify(exactly = 1) { repository.findByEmail(test.email) }
    }

    @Test
    fun findAssociationByEmailNotFound() = runTest {
        coEvery { repository.findByEmail(test.email) } returns null

        val exception =assertThrows<AssociationNotFoundException> { service.findAssociationByEmail(test.email) }
        assertEquals("No se ha encontrado una asociación con email ${test.email}", exception.message)

        coVerify(exactly = 1) { repository.findByEmail(test.email) }
    }

    @Test
    fun findAssociationById() = runTest{
        coEvery { repository.findById(test.id) } returns test

        val find = service.findAssociationById(test.id)
        assertAll(
            { assertEquals(test.id, find.id) },
            { assertEquals(test.name, find.name) },
            { assertEquals(test.email, find.email) },
            { assertEquals(test.password, find.password) },
            { assertEquals(test.username, find.username) },
            { assertEquals(test.description, find.description) },
            { assertEquals(test.url, find.url) },
            { assertEquals(test.rol, find.rol) }
        )

        coVerify(exactly = 1) { repository.findById(test.id) }
    }

    @Test
    fun findAssociationByUuidNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<AssociationNotFoundException> { service.findAssociationById(test.id) }
        assertEquals("No se ha encontrado una asociación con id ${test.id}", exception.message)

        coVerify(exactly = 1) { repository.findById(test.id) }
    }

    @Test
    fun saveAssociation() = runTest {
        coEvery { repository.findByEmail(any()) } returns null
        coEvery { repository.save(any()) } returns test
        coEvery { bcryptService.encryptPassword(any()) } returns test.password

        val created = service.saveAssociation(createTest)
        assertAll(
            { assertEquals(test.id, created.id) },
            { assertEquals(test.name, created.name) },
            { assertEquals(test.email, created.email) },
            { assertEquals(test.password, created.password) },
            { assertEquals(test.username, created.username) },
            { assertEquals(test.description, created.description) },
            { assertEquals(test.url, created.url) },
            { assertEquals(test.rol, created.rol) }
        )

        coVerify(exactly = 1) {repository.findByEmail(any()) }
        coVerify(exactly = 1) { repository.save(any()) }
        coVerify(exactly = 1) { bcryptService.encryptPassword(any()) }
    }

    @Test
    fun saveAssociationExists() = runTest{
        coEvery { repository.findByEmail(any()) } returns test
        val exception = assertThrows<AssociationBadRequestException> { service.saveAssociation(createTest) }
        assertEquals("Ya existe una asociación con email ${test.email}", exception.message)

        coVerify(exactly = 1) {repository.findByEmail(any())}
    }

    @Test
    fun updateAssociation() = runTest {
        coEvery { repository.findByEmail(test.email) } returns null
        coEvery { repository.findById(test.id) } returns test
        coEvery { bcryptService.encryptPassword(any()) } returns test.password
        coEvery { repository.update(test) } returns test

        val updated = service.updateAssociation(createTest, test.id)
        assertAll(
            { assertEquals(test.id, updated.id) },
            { assertEquals(test.name, updated.name) },
            { assertEquals(test.email, updated.email) },
            { assertEquals(test.password, updated.password) },
            { assertEquals(test.username, updated.username) },
            { assertEquals(test.description, updated.description) },
            { assertEquals(test.url, updated.url) },
            { assertEquals(test.rol, updated.rol) }
        )

        coVerify(exactly = 1) { repository.findByEmail(test.email) }
        coVerify(exactly = 1) { repository.findById(test.id) }
        coVerify(exactly = 1) { repository.update(test) }
        coVerify(exactly = 1) { bcryptService.encryptPassword(any()) }
    }

    @Test
    fun updateAssociationNotFound() = runTest {
        coEvery { repository.findByEmail(any()) } returns null
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<AssociationNotFoundException> { service.updateAssociation(createTest, test.id) }
        assertEquals("No se ha encontrado una asociación con id ${test.id}", exception.message)

        coVerify(exactly = 1) { repository.findById(test.id) }
        coVerify(exactly = 1) {repository.findByEmail(any())}
    }

    @Test
    fun updateAssociationExists() = runTest {
        coEvery { repository.findByEmail(any()) } returns test

        val exception = assertThrows<AssociationBadRequestException> { service.updateAssociation(createTest, test.id) }
        assertEquals("Ya existe una asociación con email ${test.email}", exception.message)

        coVerify(exactly = 1) { repository.findByEmail(any()) }
    }

    @Test
    fun deleteAssociation() = runTest {
        coEvery { repository.findById(test.id) } returns test
        coEvery { repository.delete(any()) } returns true

        val deleted = service.deleteAssociation(test.id)
        assertTrue(deleted)

        coVerify(exactly = 1) { repository.findById(test.id) }
        coVerify(exactly = 1) { repository.delete(any()) }
    }

    @Test
    fun deleteAssociationNotFound() = runTest {
        coEvery { repository.findById(test.id) } returns null

        val exception = assertThrows<AssociationNotFoundException> { service.deleteAssociation(test.id) }
        assertEquals("No se ha encontrado una asociación con id ${test.id}", exception.message)

        coVerify(exactly = 1) { repository.findById(test.id) }
    }

    @Test
    fun findAllAssociations() = runTest {
        coEvery { repository.findAll() } returns flowOf(test)

        val find = service.findAllAssociations()
        assertAll(
            { assertTrue(find.isNotEmpty()) },
            { assertEquals(test.id, find[0].id) },
            { assertEquals(test.name, find[0].name) },
            { assertEquals(test.email, find[0].email) },
            { assertEquals(test.password, find[0].password) },
            { assertEquals(test.username, find[0].username) },
            { assertEquals(test.description, find[0].description) },
            { assertEquals(test.url, find[0].url) },
            { assertEquals(test.rol, find[0].rol) }
        )

        coVerify(exactly = 1) { repository.findAll() }
    }

    @Test
    fun deleteAllAssociations() = runTest {
        coEvery { repository.deleteAll() } returns true

        val deleted = service.deleteAllAssociations()
        assertTrue(deleted)

        coVerify(exactly = 1) { repository.deleteAll() }
    }
}