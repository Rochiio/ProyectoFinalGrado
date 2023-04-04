package com.example.models

import com.example.serializer.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.util.*

@Serializable
data class User(
    @BsonId @Contextual
    val id: String = newId<User>().toString(),
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID =UUID.randomUUID(),
    val name: String,
    val email: String,
    val password: String,
    val username: String,
    val rol: Rol = Rol.USER
)
