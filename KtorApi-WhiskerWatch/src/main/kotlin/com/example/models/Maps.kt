package com.example.models

import com.example.serializer.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.util.*

@Serializable
data class Maps(
    @BsonId @Contextual
    val id: String = newId<Maps>().toString(),
    @Serializable(with = UUIDSerializer::class)
    val uuid:UUID = UUID.randomUUID(),
    val latitude: String,
    val longitude: String
)
