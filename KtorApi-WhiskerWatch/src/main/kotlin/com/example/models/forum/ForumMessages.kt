package com.example.models.forum

import com.example.serializer.LocalDateSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.time.LocalDate

@Serializable
data class ForumMessages(
    @BsonId @Contextual
    val id: String = newId<ForumMessages>().toString(),
    var username: String,
    var message: String,
    @Serializable(with = LocalDateSerializer::class)
    var created_At: LocalDate = LocalDate.now()
)
