package com.example.routing

import com.example.dto.*
import com.example.models.Maps
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
class MapRoutesTest {
    private val config = ApplicationConfig("application.conf")

    private var createUser = UserCreateDto(name = "test", email = "testmap@gmail.com", password = "123456789", username = "test", rol = Rol.ADMIN.name)
    private var loginUser = UserLogin(email="testmap@gmail.com", password="123456789")

    private var test = Maps(latitude = "1235.2", longitude = "12.54")
    private var createTest = MapsCreateDto(latitude = "1235.2", longitude = "12.54")


    @Test
    @Order(1)
    fun testPost() = testApplication {
        environment { config }
        var client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/user/register") {
            contentType(ContentType.Application.Json)
            setBody(createUser)
        }
        var response = client.post("/user/login") {
            contentType(ContentType.Application.Json)
            setBody(loginUser)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val res = json.decodeFromString<UserWithTokenDto>(response.bodyAsText())

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

        response = client.post("/map") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val post = json.decodeFromString<Maps>(response.bodyAsText())
        assertAll(
            { assertEquals(test.latitude, post.latitude) },
            { assertEquals(test.longitude, post.longitude) }
        )

        response = client.delete("/map/${post.id}")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }


    @Test
    @Order(2)
    fun testGetAll() = testApplication {
        environment { config }
        var client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/user/register") {
            contentType(ContentType.Application.Json)
            setBody(createUser)
        }
        var response = client.post("/user/login") {
            contentType(ContentType.Application.Json)
            setBody(loginUser)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val res = json.decodeFromString<UserWithTokenDto>(response.bodyAsText())

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

        response = client.post("/map") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val post = json.decodeFromString<Maps>(response.bodyAsText())

        response = client.get("/map")
        assertEquals(HttpStatusCode.OK, response.status)
        val get = json.decodeFromString<List<Maps>>(response.bodyAsText())
        assertAll(
            { assertTrue(get.isNotEmpty()) },
            { assertEquals(test.latitude, get[0].latitude) },
            { assertEquals(test.longitude, get[0].longitude) }
        )

        response = client.delete("/map/${post.id}")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }


    @Test
    @Order(3)
    fun testGetById() = testApplication {
        environment { config }
        var client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/user/register") {
            contentType(ContentType.Application.Json)
            setBody(createUser)
        }
        var response = client.post("/user/login") {
            contentType(ContentType.Application.Json)
            setBody(loginUser)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val res = json.decodeFromString<UserWithTokenDto>(response.bodyAsText())

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

        response = client.post("/map") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val post = json.decodeFromString<Maps>(response.bodyAsText())

        response = client.get("/map/${post.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        val get = json.decodeFromString<Maps>(response.bodyAsText())
        assertAll(
            { assertEquals(post.id, get.id) },
            { assertEquals(test.latitude, get.latitude) },
            { assertEquals(test.longitude, get.longitude) }
        )

        response = client.delete("/map/${post.id}")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }


    @Test
    @Order(4)
    fun testPut() = testApplication {
        environment { config }
        var client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/user/register") {
            contentType(ContentType.Application.Json)
            setBody(createUser)
        }
        var response = client.post("/user/login") {
            contentType(ContentType.Application.Json)
            setBody(loginUser)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val res = json.decodeFromString<UserWithTokenDto>(response.bodyAsText())

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

        response = client.post("/map") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val post = json.decodeFromString<Maps>(response.bodyAsText())

        val updated = createTest.copy(latitude = "12.12")
        response = client.put("/map/${post.id}"){
            contentType(ContentType.Application.Json)
            setBody(updated)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val update = json.decodeFromString<Maps>(response.bodyAsText())
        assertAll(
            { assertEquals(post.id, update.id) },
            { assertEquals(updated.latitude, update.latitude) },
            { assertEquals(updated.longitude, update.longitude) }
        )

        response = client.delete("/map/${post.id}")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }


    @Test
    @Order(5)
    fun testDelete() = testApplication {
        environment { config }
        var client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        client.post("/user/register") {
            contentType(ContentType.Application.Json)
            setBody(createUser)
        }
        var response = client.post("/user/login") {
            contentType(ContentType.Application.Json)
            setBody(loginUser)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val res = json.decodeFromString<UserWithTokenDto>(response.bodyAsText())

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

        response = client.post("/map") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val post = json.decodeFromString<Maps>(response.bodyAsText())

        response = client.delete("/map/${post.id}")
        assertEquals(HttpStatusCode.NoContent, response.status)

        client.delete("/user/${res.user.id}")
    }
}