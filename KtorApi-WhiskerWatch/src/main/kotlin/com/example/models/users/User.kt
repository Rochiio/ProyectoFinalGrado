package com.example.models.users

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
    var name: String,
    var email: String,
    var password: String,
    var username: String,
    var rol: Rol = Rol.USER
)
