package com.example.models

import com.fasterxml.jackson.databind.ser.std.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.util.*

@Serializable
data class Association(
    @BsonId @Contextual
    val id: String = newId<Association>().toString(),
    @Serializable(with = com.example.serializer.UUIDSerializer::class)
    val uuid: UUID = UUID.randomUUID(),
    var name: String,
    var email: String,
    var username: String,
    var password: String,
    var description: String,
    var url: String,
    var image: String? = null
)
