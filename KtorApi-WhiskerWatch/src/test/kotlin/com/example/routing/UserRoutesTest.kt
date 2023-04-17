package com.example.routing

import com.example.dto.UserCreateDto
import com.example.dto.UserWithTokenDto
import com.example.models.users.Rol
import com.example.models.users.User
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

private val json = Json { ignoreUnknownKeys = true }

//TODO Mirar
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserRoutesTest {
    private val config = ApplicationConfig("application.conf")

    private var test = User(name = "test", email = "test@gmail.com", password = "123456789", username = "test")
    private var createTest = UserCreateDto(name = "test", email = "test@gmail.com", password = "123456789", username = "test", rol = Rol.USER.name)
    private var token:String =""

    @Test
    @Order(1)
    fun testPostRegister() = testApplication {
        environment { config }
        val client = createClient {
            install(ContentNegotiation){
                json()
            }
        }
        val response = client.post("/user/register"){
            contentType(ContentType.Application.Json)
            setBody(createTest)
        }
        val result = response.bodyAsText()
        val user = json.decodeFromString<UserWithTokenDto>(result)
        token = user.token
        assertAll(
            { assertEquals(HttpStatusCode.Created, response.status) },
            { assertEquals(user.user.name, createTest.name) },
            { assertEquals(user.user.email, createTest.email) },
            { assertEquals(user.user.username, createTest.username) }
        )
    }


    @Test
    @Order(2)
    fun testGetAll() = testApplication {
        environment { config }
        val response = client.get("/user"){
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    @Order(3)
    fun testGetById() = testApplication {
        environment { config }
        val response = client.get("/user/${test.id}"){
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    @Order(4)
    fun testPut() = testApplication {
        environment { config }
        val client = createClient {
            install(ContentNegotiation){
                json()
            }
        }
        val response = client.put("/user/${test.id}"){
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(createTest)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    @Order(5)
    fun testDelete() = testApplication {
        environment { config }
        val response = client.delete("/user/${test.id}"){
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

}