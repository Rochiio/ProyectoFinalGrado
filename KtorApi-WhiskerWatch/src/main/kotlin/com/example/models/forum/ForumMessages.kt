package com.example.models.forum

import com.example.serializer.LocalDateSerializer
import com.example.serializer.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.time.LocalDate
import java.util.*

@Serializable
data class ForumMessages(
    @BsonId @Contextual
    val id: String = newId<ForumMessages>().toString(),
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID = UUID.randomUUID(),
    var username: String,
    var message: String,
    @Serializable(with = LocalDateSerializer::class)
    var created_At: LocalDate = LocalDate.now()
)
