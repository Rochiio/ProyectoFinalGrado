package com.example.plugins

import com.example.config.StorageConfig
import com.example.services.storage.Storage
import io.ktor.server.application.*
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject

fun Application.configureStorage(){
    val config = mapOf(
        "imagesDir" to environment.config.property("storage.imagesDir").getString()
    )

    val storageConfig: StorageConfig = get{ parametersOf(config)}
    val storageService: Storage by inject()

    storageService.initConfiguration()
}