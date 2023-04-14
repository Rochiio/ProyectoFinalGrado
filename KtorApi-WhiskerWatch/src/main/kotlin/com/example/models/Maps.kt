package com.example.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId

@Serializable
data class Maps(
    @BsonId @Contextual
    val id: String = newId<Maps>().toString(),
    var latitude: String,
    var longitude: String
)
