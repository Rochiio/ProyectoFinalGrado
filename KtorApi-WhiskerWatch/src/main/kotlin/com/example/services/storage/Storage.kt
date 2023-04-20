package com.example.services.storage

import io.ktor.http.content.*
import java.io.File

interface Storage {
    fun initConfiguration()
    suspend fun saveFile(fileName: String, item: PartData.FileItem): Map<String,String>
    suspend fun getFile(fileName: String): File?
    suspend fun deleteFile(fileName: String): Boolean
}