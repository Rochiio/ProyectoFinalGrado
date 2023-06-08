package com.example.services.storage

import com.example.config.StorageConfig
import io.ktor.http.content.*
import mu.KotlinLogging
import org.koin.core.annotation.Single
import java.io.File
import java.time.LocalDate

@Single
class StorageServiceImpl(
    private val storageConfig: StorageConfig
): Storage {
    private val logger = KotlinLogging.logger{}

    override fun initConfiguration() {
        if(!File(storageConfig.imagesDir).exists()){
            File(storageConfig.imagesDir).mkdir()
        }
    }

    override suspend fun saveFile(fileName: String, item: PartData.FileItem): Map<String, String> {
        logger.info { "Salvando fichero : $fileName" }
        val fileBytes = item.streamProvider().readBytes()
        val fileExtension = item.originalFileName?.takeLastWhile { it != '.' }
        val name = "$fileName.$fileExtension"
        File("${storageConfig.imagesDir}${File.separator}$name").writeBytes(fileBytes)
        return mapOf("FileName" to name, "FileExtension" to fileExtension.toString(), "created_At" to LocalDate.now().toString())
    }

    override suspend fun getFile(fileName: String): File? {
        logger.info { "Consiguiendo el fichero con el nombre : $fileName" }
        val file = File("${storageConfig.imagesDir}/$fileName")
        if(!file.exists()){
            return null
        }
        return file
    }

    override suspend fun deleteFile(fileName: String): Boolean {
        logger.info { "Eliminando fichero con el nombre : $fileName" }
        val file = File("${storageConfig.imagesDir}/$fileName")
        if(file.exists()){
            file.delete()
            return true
        }
        return false
    }
}