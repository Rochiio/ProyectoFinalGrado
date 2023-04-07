package com.example.models.calendar

import com.example.models.forum.ForumMessages
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
    val id: String = newId<ForumMessages>().toString(),
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID = UUID.randomUUID(),
    @Serializable( with = LocalDateSerializer::class)
    var date: LocalDate,
    var task: String
)
