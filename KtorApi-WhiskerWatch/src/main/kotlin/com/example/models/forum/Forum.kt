package com.example.models.forum

import com.example.serializer.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.util.*

@Serializable
data class  Forum(
    @BsonId @Contextual
    val id: String = newId<Forum>().toString(),
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID = UUID.randomUUID(),
    @Serializable(with = UUIDSerializer::class)
    var mapsUuid: UUID,
    var listMessages: MutableList<ForumMessages>
)
