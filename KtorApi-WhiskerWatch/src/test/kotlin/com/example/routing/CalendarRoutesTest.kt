package com.example.routing

import com.example.dto.*
import com.example.models.Maps
import com.example.models.calendar.Calendar
import com.example.models.calendar.Task
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
import java.time.LocalDate
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val json = Json { ignoreUnknownKeys = true }

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CalendarRoutesTest {
    private val config = ApplicationConfig("application.conf")

    private var createUser = UserCreateDto(name = "test", email = "testcal@gmail.com", password = "123456789", username = "test", rol = Rol.ADMIN.name)
    private var loginUser = UserLogin(email="testcal@gmail.com", password="123456789")

    private val test = Calendar(
        mapsId = "5dc236c1-ef36-439e-b9c6-04b7dd145d36",
        listTasks = mutableListOf(Task(date = LocalDate.now().toString(), task = "tarea test"))
    )
    private val createTest = CalendarCreateDto(mapsId = "5dc236c1-ef36-439e-b9c6-04b7dd145d36",
        listTasks = mutableListOf(TaskCreateDto(date = LocalDate.now().toString(), task = "tarea test")))

    private var createMap = MapsCreateDto(latitude = "1235.2", longitude = "12.54")

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
            setBody(createMap)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val postMap = json.decodeFromString<Maps>(response.bodyAsText())

        createTest.mapsId = postMap.id

        response = client.post("/calendar") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val post = json.decodeFromString<Calendar>(response.bodyAsText())
        assertAll(
            { assertEquals(postMap.id, post.mapsId) }
        )

        response = client.delete("/calendar/${post.id}")
        assertEquals(HttpStatusCode.NoContent, response.status)

        response = client.delete("/map/${postMap.id}")
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
            setBody(createMap)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val postMap = json.decodeFromString<Maps>(response.bodyAsText())

        createTest.mapsId = postMap.id

        client.post("/calendar") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }

        response = client.get("/calendar")
        assertEquals(HttpStatusCode.OK, response.status)
        val get = json.decodeFromString<List<Calendar>>(response.bodyAsText())
        assertAll(
            { assertTrue(get.isNotEmpty()) },
            { assertEquals(postMap.id, get[0].mapsId) }
        )

        response = client.delete("/calendar/${get[0].id}")
        assertEquals(HttpStatusCode.NoContent, response.status)

        response = client.delete("/map/${postMap.id}")
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
            setBody(createMap)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val postMap = json.decodeFromString<Maps>(response.bodyAsText())

        createTest.mapsId = postMap.id

        response = client.post("/calendar") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val post = json.decodeFromString<Calendar>(response.bodyAsText())

        response = client.get("/calendar/${post.id}")
        assertEquals(HttpStatusCode.OK, response.status)
        val get = json.decodeFromString<Calendar>(response.bodyAsText())
        assertAll(
            { assertEquals(post.id, get.id) },
            { assertEquals(postMap.id, get.mapsId) }
        )

        response = client.delete("/calendar/${post.id}")
        assertEquals(HttpStatusCode.NoContent, response.status)

        response = client.delete("/map/${postMap.id}")
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
            setBody(createMap)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val postMap = json.decodeFromString<Maps>(response.bodyAsText())

        createTest.mapsId = postMap.id

        response = client.post("/calendar") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val post = json.decodeFromString<Calendar>(response.bodyAsText())

        val updated = createTest.copy(mapsId = postMap.id)
        response = client.put("/calendar/${post.id}"){
            contentType(ContentType.Application.Json)
            setBody(updated)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val update = json.decodeFromString<Calendar>(response.bodyAsText())
        assertAll(
            { assertEquals(post.id, update.id) },
            { assertEquals(updated.mapsId, update.mapsId) }
        )

        response = client.delete("/calendar/${post.id}")
        assertEquals(HttpStatusCode.NoContent, response.status)

        response = client.delete("/map/${postMap.id}")
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
            setBody(createMap)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val postMap = json.decodeFromString<Maps>(response.bodyAsText())

        createTest.mapsId = postMap.id

        response = client.post("/calendar") {
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        assertEquals(HttpStatusCode.Created, response.status)
        val post = json.decodeFromString<Calendar>(response.bodyAsText())

        response = client.delete("/calendar/${post.id}")
        assertEquals(HttpStatusCode.NoContent, response.status)

        client.delete("/user/${res.user.id}")

        response = client.delete("/map/${postMap.id}")
        assertEquals(HttpStatusCode.NoContent, response.status)
    }
}