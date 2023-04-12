package com.example.models.calendar

import com.example.serializer.LocalDateSerializer
import com.example.serializer.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.time.LocalDate
import java.util.*

@Serializable
data class Task(
    @BsonId @Contextual
    val id: String = newId<Task>().toString(),
    val uuid: String = UUID.randomUUID().toString(),
    @Serializable( with = LocalDateSerializer::class)
    var date: LocalDate,
    var task: String
)
