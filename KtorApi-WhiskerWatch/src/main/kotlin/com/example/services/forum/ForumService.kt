package com.example.services.forum

import com.example.dto.ForumCreateDto
import com.example.exception.ForumBadRequestException
import com.example.exception.ForumNotFoundException
import com.example.exception.MapsNotFoundException
import com.example.exception.UUIDBadRequestException
import com.example.mappers.toListMessages
import com.example.models.forum.Forum
import com.example.repositories.forum.ForumRepository
import com.example.utils.toUUID
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single
import java.util.*

@Single
class ForumService(
    private val forumRepository: ForumRepository
) {

    suspend fun findByMapsUuid(uuid: String): Forum {
        return forumRepository.findByMapsUuid(uuid)
            ?: throw MapsNotFoundException("No se ha encontrado un foro con uuid de mapa $uuid")
    }

    suspend fun findByUuid(uuid: String): Forum {
        return forumRepository.findByUUID(uuid)
            ?: throw ForumNotFoundException("No se ha encontrado un foro con uuid $uuid")
    }

    suspend fun saveForum(forum: ForumCreateDto): Forum {
        val created = Forum(
            mapsUuid = forum.mapsUuid,
            listMessages = forum.listMessages.toListMessages().toMutableList()
        )
        return forumRepository.save(created)
    }

    suspend fun updateForum(forum: ForumCreateDto, uuidForum: String): Forum {
        val find = forumRepository.findByUUID(uuidForum)
        find?.let {
            val list = it.listMessages
            val newList = forum.listMessages.toListMessages()
            val updated = Forum(
                id = it.id, uuid = it.uuid,
                mapsUuid = forum.mapsUuid,
                listMessages = (list + newList).toMutableList()
            )
            return forumRepository.update(updated)
        } ?: run {
            throw ForumNotFoundException("No se ha encontrado un foro con uuid $uuidForum")
        }
    }

    suspend fun deleteForum(uuid: String): Boolean {
        val find = forumRepository.findByUUID(uuid)
        find?.let {
            return forumRepository.delete(it)
        }?: run{
            throw ForumNotFoundException("No se ha encontrado un foro con uuid $uuid")
        }
    }

    suspend fun findAllForums(): List<Forum>{
        return forumRepository.findAll().toList()
    }

    suspend fun deleteAllForums(): Boolean{
        return forumRepository.deleteAll()
    }

}