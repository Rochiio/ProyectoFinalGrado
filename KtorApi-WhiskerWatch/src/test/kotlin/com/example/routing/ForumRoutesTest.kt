package com.example.routing

import com.example.dto.*
import com.example.models.forum.Forum
import com.example.models.forum.ForumMessages
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
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val json = Json { ignoreUnknownKeys = true }


// TODO Mirar no se borran todos
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ForumRoutesTest {
    private val config = ApplicationConfig("application.conf")

    private var createUser = UserCreateDto(name = "test", email = "test@gmail.com", password = "123456789", username = "test", rol = Rol.USER.name)
    private var loginUser = UserLogin(email="test@gmail.com", password="123456789")

    private val test = Forum(
        mapsId = "3544e9ee-42f0-43f5-b39e-436c9d8828c9", listMessages =
        mutableListOf(ForumMessages(username = "pepe", message = "test"))
    )

    private val createTest = ForumCreateDto(mapsId = "3544e9ee-42f0-43f5-b39e-436c9d8828c9", listMessages =
    mutableListOf(ForumMessagesCreateDto(username = "pepe", message = "test", created_At = "2023-03-15")))


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

        response = client.post("/forum") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val post = json.decodeFromString<Forum>(response.bodyAsText())
        assertAll(
            { assertEquals(test.mapsId, post.mapsId) }
        )
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

        client.post("/forum") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }

        response = client.get("/forum")
        assertEquals(HttpStatusCode.OK, response.status)
        val get = json.decodeFromString<List<Forum>>(response.bodyAsText())
        assertAll(
            { assertTrue(get.isNotEmpty()) },
            { assertEquals(test.mapsId, get[0].mapsId) }
        )
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

        response = client.post("/forum") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val post = json.decodeFromString<Forum>(response.bodyAsText())

        response = client.get("/forum/${post.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        val get = json.decodeFromString<Forum>(response.bodyAsText())
        assertAll(
            { assertEquals(post.id, get.id) },
            { assertEquals(test.mapsId, get.mapsId) }
        )
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

        response = client.post("/forum") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val post = json.decodeFromString<Forum>(response.bodyAsText())

        val updated = createTest.copy(mapsId = "da0b0753-312b-4126-812a-d388ca3c305b")
        response = client.put("/forum/${post.id}"){
            contentType(ContentType.Application.Json)
            setBody(updated)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val update = json.decodeFromString<Forum>(response.bodyAsText())
        assertAll(
            { assertEquals(post.id, update.id) },
            { assertEquals(updated.mapsId, update.mapsId) }
        )
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

        response = client.post("/forum") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val post = json.decodeFromString<Forum>(response.bodyAsText())

        response = client.delete("/forum/${post.id}")
        assertEquals(HttpStatusCode.NoContent, response.status)

        client.delete("/user/${res.user.id}")
    }
}