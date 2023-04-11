package com.example.mappers

import com.example.dto.ForumMessagesCreateDto
import com.example.models.forum.ForumMessages
import java.time.LocalDate

/**
 * Mapeador para pasar la lista de mensajes de un foro CreateDTO a Foro
 */
fun List<ForumMessagesCreateDto>.toListMessages(): List<ForumMessages>{
    val list: MutableList<ForumMessages> = mutableListOf()
    this.forEach {
        val forum = ForumMessages(username = it.username, message = it.message,
            created_At = LocalDate.now())
        list.add(forum)
    }
    return list
}

