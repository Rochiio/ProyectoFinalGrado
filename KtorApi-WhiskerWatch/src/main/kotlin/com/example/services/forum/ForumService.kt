package com.example.services.forum

import com.example.dto.ForumCreateDto
import com.example.error.ForumError
import com.example.mappers.toListMessages
import com.example.models.forum.Forum
import com.example.repositories.forum.ForumRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single

@Single
class ForumService(
    private val forumRepository: ForumRepository
) {

    suspend fun findByMapsId(id: String): Result<Forum, ForumError> {
        return forumRepository.findByMapsId(id)?.let {
            Ok(it)
        }?: run{
            Err(ForumError.ForumNotFoundError("No se ha encontrado un foro con id de mapa $id"))
        }
    }

    suspend fun findById(id: String): Result<Forum, ForumError> {
        return forumRepository.findById(id)?.let {
            Ok(it)
        }?: run{
            Err(ForumError.ForumNotFoundError("No se ha encontrado un foro con id $id"))
        }
    }

    suspend fun saveForum(forum: ForumCreateDto): Result<Forum, ForumError> {
        val created = Forum(
            mapsId = forum.mapsId,
            listMessages = forum.listMessages.toListMessages().toMutableList()
        )
        return Ok(forumRepository.save(created))
    }

    suspend fun updateForum(forum: ForumCreateDto, idForum: String): Result<Forum, ForumError> {
        return forumRepository.findById(idForum)?.let {
            val list = it.listMessages
            val newList = forum.listMessages.toListMessages()
            val updated = Forum(
                id = it.id, mapsId = forum.mapsId,
                listMessages = (list + newList).toMutableList()
            )
            Ok(forumRepository.update(updated))
        } ?: run {
            Err(ForumError.ForumNotFoundError("No se ha encontrado un foro con id $idForum"))
        }
    }

    suspend fun deleteForum(id: String): Result<Boolean, ForumError> {
        return forumRepository.findById(id)?.let {
            Ok(forumRepository.delete(it))
        }?: run{
            Err(ForumError.ForumNotFoundError("No se ha encontrado un foro con id $id"))
        }
    }

    suspend fun findAllForums(): List<Forum>{
        return forumRepository.findAll().toList()
    }

    suspend fun deleteAllForums(): Boolean{
        return forumRepository.deleteAll()
    }

}