package com.example.models.users

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId

@Serializable
data class User(
    @BsonId @Contextual
    val id: String = newId<User>().toString(),
    var name: String,
    var email: String,
    var password: String,
    var username: String,
    var rol: Rol = Rol.USER,
)

