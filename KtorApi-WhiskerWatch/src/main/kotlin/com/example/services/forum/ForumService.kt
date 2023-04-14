package com.example.services.forum

import com.example.dto.ForumCreateDto
import com.example.exception.ForumNotFoundException
import com.example.exception.MapsNotFoundException
import com.example.mappers.toListMessages
import com.example.models.forum.Forum
import com.example.repositories.forum.ForumRepository
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single

@Single
class ForumService(
    private val forumRepository: ForumRepository
) {

    suspend fun findByMapsId(id: String): Forum {
        return forumRepository.findByMapsId(id)
            ?: throw MapsNotFoundException("No se ha encontrado un foro con id de mapa $id")
    }

    suspend fun findById(id: String): Forum {
        return forumRepository.findById(id)
            ?: throw ForumNotFoundException("No se ha encontrado un foro con id $id")
    }

    suspend fun saveForum(forum: ForumCreateDto): Forum {
        val created = Forum(
            mapsId = forum.mapsId,
            listMessages = forum.listMessages.toListMessages().toMutableList()
        )
        return forumRepository.save(created)
    }

    suspend fun updateForum(forum: ForumCreateDto, idForum: String): Forum {
        val find = forumRepository.findById(idForum)
        find?.let {
            val list = it.listMessages
            val newList = forum.listMessages.toListMessages()
            val updated = Forum(
                id = it.id, mapsId = forum.mapsId,
                listMessages = (list + newList).toMutableList()
            )
            return forumRepository.update(updated)
        } ?: run {
            throw ForumNotFoundException("No se ha encontrado un foro con id $idForum")
        }
    }

    suspend fun deleteForum(id: String): Boolean {
        val find = forumRepository.findById(id)
        find?.let {
            return forumRepository.delete(it)
        }?: run{
            throw ForumNotFoundException("No se ha encontrado un foro con id $id")
        }
    }

    suspend fun findAllForums(): List<Forum>{
        return forumRepository.findAll().toList()
    }

    suspend fun deleteAllForums(): Boolean{
        return forumRepository.deleteAll()
    }

}