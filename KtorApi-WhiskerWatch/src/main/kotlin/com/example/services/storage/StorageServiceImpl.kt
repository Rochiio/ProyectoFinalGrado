package com.example.services.storage

import com.example.config.StorageConfig
import io.ktor.http.content.*
import org.koin.core.annotation.Single
import java.io.File
import java.time.LocalDate

@Single
class StorageServiceImpl(
    private val storageConfig: StorageConfig
): Storage {

    override fun initConfiguration() {
        if(!File(storageConfig.imagesDir).exists()){
            File(storageConfig.imagesDir).mkdir()
        }
    }

    override suspend fun saveFile(fileName: String, item: PartData.FileItem): Map<String, String> {
        val fileBytes = item.streamProvider().readBytes()
        val fileExtension = item.originalFileName?.takeLastWhile { it != '.' }
        val name = "$fileName.$fileExtension"
        File("${storageConfig.imagesDir}${File.separator}$name").writeBytes(fileBytes)
        return mapOf("FileName" to name, "FileExtension" to fileExtension.toString(), "created_At" to LocalDate.now().toString())
    }

    override suspend fun getFile(fileName: String): File? {
        val file = File("${storageConfig.imagesDir}/$fileName")
        if(!file.exists()){
            return null
        }
        return file
    }

    override suspend fun deleteFile(fileName: String): Boolean {
        val file = File("${storageConfig.imagesDir}/$fileName")
        if(!file.exists()){
            file.delete()
            return true
        }
        return false
    }
}