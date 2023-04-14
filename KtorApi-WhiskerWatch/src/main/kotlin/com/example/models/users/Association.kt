package com.example.models.users

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId

@Serializable
data class Association(
    @BsonId @Contextual
    val id: String = newId<Association>().toString(),
    var name: String,
    var email: String,
    var username: String,
    var password: String,
    val rol: Rol = Rol.ASSOCIATION,
    var description: String,
    var url: String,
    var image: String? = null
)
