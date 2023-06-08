package com.example.config

import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Single

/**
 * Variable de configuracion para el almacenado de imagenes
 */
@Single
data class StorageConfig(
    @InjectedParam private val config: Map<String,String>
){
    val imagesDir = config["imagesDir"].toString()
}
