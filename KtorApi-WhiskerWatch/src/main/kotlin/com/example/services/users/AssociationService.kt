package com.example.services.users

import com.example.dto.AssociationCreateDto
import com.example.exception.AssociationNotFoundException
import com.example.models.users.Association
import com.example.models.users.Rol
import com.example.repositories.users.AssociationRepository
import com.example.services.password.BcryptService
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single
import java.util.UUID

@Single
class AssociationService(
    private val associationRepository: AssociationRepository,
    private val passwordEncoder: BcryptService
) {

    suspend fun findAssociationByEmail(email: String): Association {
        return associationRepository.findByEmail(email)
            ?: throw AssociationNotFoundException("No se ha encontrado una asociación con email $email")
    }

    suspend fun findAssociationByUuid(uuid: String): Association{
        return associationRepository.findByUUID(uuid)
            ?: throw AssociationNotFoundException("No se ha encontrado una asociación con uuid $uuid")
    }

    suspend fun saveAssociation(association: AssociationCreateDto): Association {
        val created = Association(name = association.name, email = association.email, username = association.username,
            password = passwordEncoder.encryptPassword(association.password), rol = Rol.valueOf(association.rol),
            description = association.description, url = association.url)
        return associationRepository.save(created )
    }

    suspend fun updateAssociation(association: AssociationCreateDto, uuidAssociation: String): Association{
        val find = associationRepository.findByUUID(uuidAssociation)
        find?.let {
            val updated = Association(id = it.id, uuid = it.uuid, name = association.name, email = association.email,
                username = association.username, password = passwordEncoder.encryptPassword(association.password),
                rol = Rol.valueOf(association.rol), description = association.description, url = association.url)
            return associationRepository.update(updated)
        }?: run{
            throw AssociationNotFoundException("No se ha encontrado una asociación con uuid $uuidAssociation")
        }
    }

    suspend fun deleteAssociation(uuid: String): Boolean{
        val find = associationRepository.findByUUID(uuid)
        find?.let {
            return associationRepository.delete(it)
        }?: run{
            throw AssociationNotFoundException("No se ha encontrado una asociación con uuid $uuid")
        }
    }

    suspend fun findAllAssociations(): List<Association>{
        return associationRepository.findAll().toList()
    }

    suspend fun deleteAllAssociations(): Boolean{
        return associationRepository.deleteAll()
    }
}