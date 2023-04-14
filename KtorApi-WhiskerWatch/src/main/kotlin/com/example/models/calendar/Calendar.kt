package com.example.models.calendar

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId

@Serializable
data class Calendar(
    @BsonId @Contextual
    val id: String = newId<Calendar>().toString(),
    var mapsId: String,
    var listTasks: MutableList<Task>
)
