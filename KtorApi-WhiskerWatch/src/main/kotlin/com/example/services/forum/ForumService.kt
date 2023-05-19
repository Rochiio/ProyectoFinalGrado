package com.example.services.forum

import com.example.dto.ForumCreateDto
import com.example.error.ForumError
import com.example.mappers.toListMessages
import com.example.models.forum.Forum
import com.example.models.forum.ForumMessages
import com.example.repositories.forum.ForumRepository
import com.example.services.maps.MapsService
import com.github.michaelbull.result.*
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single

@Single
class ForumService(
    private val forumRepository: ForumRepository,
    private val mapsService: MapsService
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
        var result: Result<Forum, ForumError>? = null
        mapsService.findMapById(forum.mapsId)
            .onSuccess {
                val created = Forum(
                    mapsId = forum.mapsId,
                    listMessages = forum.listMessages.toListMessages().toMutableList()
                )
                result = Ok(forumRepository.save(created))
            }
            .onFailure {
                result = Err(ForumError.ForumNotFoundError(it.message))
            }
        return result!!
    }

    suspend fun updateForum(forum: ForumCreateDto, idForum: String): Result<Forum, ForumError> {
        var result: Result<Forum, ForumError>? = null
        mapsService.findMapById(forum.mapsId)
            .onSuccess {
                forumRepository.findById(idForum)?.let {
                    val listToAdd = mutableSetOf<ForumMessages>()
                    it.listMessages.forEach { message -> listToAdd.add(message) }
                    forum.listMessages.toListMessages().forEach { message -> listToAdd.add(message) }
                    val updated = Forum(
                        id = it.id, mapsId = forum.mapsId,
                        listMessages = listToAdd.toMutableList()
                    )
                    result = Ok(forumRepository.update(updated))
                } ?: run {
                    result = Err(ForumError.ForumNotFoundError("No se ha encontrado un foro con id $idForum"))
                }
            }
            .onFailure {
                result = Err(ForumError.ForumNotFoundError(it.message))
            }
        return result!!
    }

    suspend fun deleteForum(id: String): Result<Boolean, ForumError> {
        return forumRepository.findById(id)?.let {
            Ok(forumRepository.delete(it))
        }?: run{
            Err(ForumError.ForumNotFoundError("No se ha encontrado un foro con id $id"))
        }
    }

    suspend fun deleteForumByMapsId(id: String): Result<Boolean, ForumError>{
        return forumRepository.findByMapsId(id)?.let {
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