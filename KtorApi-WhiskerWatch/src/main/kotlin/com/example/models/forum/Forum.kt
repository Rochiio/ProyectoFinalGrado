package com.example.models.forum

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId

@Serializable
data class  Forum(
    @BsonId @Contextual
    val id: String = newId<Forum>().toString(),
    var mapsId: String,
    var listMessages: MutableList<ForumMessages>
)
