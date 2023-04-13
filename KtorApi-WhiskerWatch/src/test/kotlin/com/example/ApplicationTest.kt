package com.example

import com.example.plugins.configureRouting
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            Assertions.assertEquals(HttpStatusCode.OK, status)
            Assertions.assertEquals("Whisker Watch API\uD83D\uDC31", bodyAsText())
        }
    }
}
