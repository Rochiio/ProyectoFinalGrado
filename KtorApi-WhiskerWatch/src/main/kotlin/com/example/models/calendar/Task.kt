package com.example.models.calendar

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId

@Serializable
data class Task(
    @BsonId @Contextual
    val id: String = newId<Task>().toString(),
    var date: String,
    var task: String
)
