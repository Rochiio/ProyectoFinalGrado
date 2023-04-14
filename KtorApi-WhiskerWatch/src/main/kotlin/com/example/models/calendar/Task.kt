package com.example.models.calendar

import com.example.serializer.LocalDateSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.time.LocalDate

@Serializable
data class Task(
    @BsonId @Contextual
    val id: String = newId<Task>().toString(),
    @Serializable( with = LocalDateSerializer::class)
    var date: LocalDate,
    var task: String
)
