package com.example.routing

import com.example.dto.*
import com.example.models.users.Rol
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val json = Json { ignoreUnknownKeys = true }

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AssociationRoutesTest {
    private val config = ApplicationConfig("application.conf")

    private var createTest = AssociationCreateDto(name = "test", email = "testaso@example.com", username = "test",
        password = "123456", description = "test description", url = "http://example.com", rol = Rol.ASSOCIATION.name)
    private var loginTest = AssociationLogin(email="testaso@example.com", password="123456")

    @Test
    @Order(1)
    fun testPostRegister() = testApplication {
        environment { config }
        val client = createClient {
            install(ContentNegotiation){
                json()
            }
        }
        val response = client.post("/association/register"){
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        val result = response.bodyAsText()
        val user = json.decodeFromString<AssociationTokenDto>(result)
        assertAll(
            { assertEquals(HttpStatusCode.Created, response.status) },
            { assertEquals(createTest.name, user.association.name) },
            { assertEquals(createTest.email, user.association.email) },
            { assertEquals(createTest.username, user.association.username) },
            { assertEquals(createTest.rol, user.association.rol) }
        )
    }

    @Test
    @Order(2)
    fun testPostLogin() = testApplication {
        environment { config }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/association/register") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }

        val responseLogin = client.post("/association/login") {
            contentType(ContentType.Application.Json)
            setBody(loginTest)
        }

        val res = json.decodeFromString<AssociationTokenDto>(responseLogin.bodyAsText())
        assertAll(
            { assertEquals(responseLogin.status, HttpStatusCode.OK) },
            { assertEquals(createTest.name, res.association.name) },
            { assertEquals(createTest.email, res.association.email) },
            { assertEquals( createTest.username, res.association.username) },
            { assertEquals(createTest.rol, res.association.rol) },
        )
    }


    @Test
    @Order(3)
    fun testGetAll() = testApplication {
        environment { config }
        var client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/association/register") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        var response = client.post("/association/login") {
            contentType(ContentType.Application.Json)
            setBody(loginTest)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val res = json.decodeFromString<AssociationTokenDto>(response.bodyAsText())

        client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(res.token, res.token)
                    }
                }
            }
        }
        response = client.get("/association")
        assertEquals(HttpStatusCode.OK, response.status)
        val list = json.decodeFromString<List<AssociationDto>>(response.bodyAsText())
        assertAll(
            { assertTrue(list.size == 1) },
            { assertEquals(createTest.name,  list[0].name) },
            { assertEquals(createTest.username,  list[0].username) },
            { assertEquals(createTest.email,  list[0].email) },
            { assertEquals(createTest.rol,  list[0].rol) }
        )
    }

    @Test
    @Order(4)
    fun testGetById() = testApplication {
        environment { config }

        var client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/association/register") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        var response = client.post("/association/login") {
            contentType(ContentType.Application.Json)
            setBody(loginTest)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val res = json.decodeFromString<AssociationTokenDto>(response.bodyAsText())

        client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(res.token, res.token)
                    }
                }
            }
        }
        response = client.get("/association/${res.association.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        val find = json.decodeFromString<AssociationDto>(response.bodyAsText())
        assertAll(
            { assertEquals(createTest.name,  find.name) },
            { assertEquals(createTest.username,  find.username) },
            { assertEquals(createTest.email,  find.email) },
            { assertEquals(createTest.rol,  find.rol) }
        )
    }

    @Test
    @Order(5)
    fun testPutUpdate() = testApplication {
        environment { config }

        var client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/association/register") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        var response = client.post("/association/login") {
            contentType(ContentType.Application.Json)
            setBody(loginTest)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val res = json.decodeFromString<AssociationTokenDto>(response.bodyAsText())

        client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(res.token, res.token)
                    }
                }
            }
        }
        val updated = createTest.copy(name = "updated")
        response = client.put("/association/${res.association.id}"){
            contentType(ContentType.Application.Json)
            setBody(updated)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val update = json.decodeFromString<AssociationDto>(response.bodyAsText())
        assertAll(
            { assertEquals(updated.name,  update.name) },
            { assertEquals(updated.username,  update.username) },
            { assertEquals(updated.email,  update.email) },
            { assertEquals(updated.rol,  update.rol) }
        )
    }

    @Test
    @Order(6)
    fun testDelete() = testApplication {
        environment { config }

        var client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/association/register") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        var response = client.post("/association/login") {
            contentType(ContentType.Application.Json)
            setBody(loginTest)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val res = json.decodeFromString<AssociationTokenDto>(response.bodyAsText())

        client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(res.token, res.token)
                    }
                }
            }
        }
        response = client.delete("/association/${res.association.id}")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }
}