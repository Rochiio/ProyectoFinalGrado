package com.example.repositories.forum

import com.example.models.forum.Forum
import com.example.repositories.ICrud
import java.util.*

interface ForumRepository: ICrud<Forum, String> {
    suspend fun findByMapsId(id: String): Forum?
}