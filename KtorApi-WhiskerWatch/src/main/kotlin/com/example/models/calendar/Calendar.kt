package com.example.models.calendar

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.newId
import java.util.*

@Serializable
data class Calendar(
    @BsonId @Contextual
    val id: String = newId<Calendar>().toString(),
    val uuid: String = UUID.randomUUID().toString(),
    var mapsUUID: String,
    var listTasks: MutableList<Task>
)
