// Ktor y Kotlin
val ktor_version: String by project
val kotlin_version: String by project

// Logger
// val logback_version: String by project
val micrologging_version: String by project
val logbackclassic_version: String by project

// Koin
val koin_ktor_version: String by project
val ksp_version: String by project
val koin_ksp_version: String by project
val koin_version: String by project

// Test
val junit_version: String by project
val mockk_version: String by project
val coroutines_version: String by project

// BCrypt
val bcrypt_version: String by project

// Ktow Swagger UI
val ktor_swagger_ui_version: String by project

val cache_version: String by project

plugins {
    kotlin("jvm") version "1.8.20"
    id("io.ktor.plugin") version "2.2.4"
    kotlin("plugin.serialization") version "1.8.20"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // Content validation
    implementation("io.ktor:ktor-server-request-validation:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logbackclassic_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    // Auth JWT
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")

    // Certificados SSL y TSL
    implementation("io.ktor:ktor-network-tls-certificates:2.2.4")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logbackclassic_version")
    implementation("io.github.microutils:kotlin-logging-jvm:$micrologging_version")

    // Koin
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")
    testImplementation("io.insert-koin:koin-test-junit4:$koin_version")

    // BCrypt
    implementation("org.mindrot:jbcrypt:$bcrypt_version")

    // Swagger
    implementation("io.github.smiley4:ktor-swagger-ui:$ktor_swagger_ui_version")

    // Cache 4K
    //implementation("io.github.reactivecircus.cache4k:cache4k:$cache_version")

    // Para testear
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version") // Usar deps de JUNIT 5 y no estas!

    // JUnit 5 en vez del por defecto de Kotlin...
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_version")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junit_version")

    // MockK para testear Mockito con Kotlin
    testImplementation("io.mockk:mockk:$mockk_version")

    // Para testear métodos suspendidos o corrutinas
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")

    // Para testear con content negotiation
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktor_version")

    // Mongo Kotlin Asincrono
    implementation("org.litote.kmongo:kmongo-async:4.7.2")

    // Corrutinas Mongo
    implementation("org.litote.kmongo:kmongo-coroutine:4.7.2")

}