package com.example.repositories.forum

import com.example.models.forum.Forum
import com.example.repositories.ICrud
import java.util.*

interface ForumRepository: ICrud<Forum, UUID> {
    suspend fun findByMapsUuid(uuid: UUID): Forum?
}