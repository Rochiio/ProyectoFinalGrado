package com.example.services.users

import com.example.dto.AssociationCreateDto
import com.example.exception.AssociationBadRequestException
import com.example.exception.AssociationNotFoundException
import com.example.models.users.Association
import com.example.models.users.Rol
import com.example.repositories.users.AssociationRepository
import com.example.services.password.BcryptService
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single

@Single
class AssociationService(
    private val associationRepository: AssociationRepository,
    private val passwordEncoder: BcryptService
) {

    suspend fun findAssociationByEmail(email: String): Association {
        return associationRepository.findByEmail(email)
            ?: throw AssociationNotFoundException("No se ha encontrado una asociación con email $email")
    }

    suspend fun findAssociationById(id: String): Association{
        return associationRepository.findById(id)
            ?: throw AssociationNotFoundException("No se ha encontrado una asociación con id $id")
    }

    suspend fun saveAssociation(association: AssociationCreateDto): Association {
        val find = associationRepository.findByEmail(association.email)
        find?.let {
            throw AssociationBadRequestException("Ya existe una asociación con email ${it.email}")
        }?: run{
            val created = Association(
                name = association.name, email = association.email, username = association.username,
                password = passwordEncoder.encryptPassword(association.password), rol = Rol.valueOf(association.rol),
                description = association.description, url = association.url
            )
            return associationRepository.save(created )
        }
    }

    suspend fun updateAssociation(association: AssociationCreateDto, idAssociation: String): Association{
        val findEmail = associationRepository.findByEmail(association.email)
        findEmail?.let {
            throw AssociationBadRequestException("Ya existe una asociación con email ${association.email}")
        }?: run{
            val find = associationRepository.findById(idAssociation)
            find?.let {
                val updated = Association(
                    id = it.id,
                    name = association.name,
                    email = association.email,
                    username = association.username,
                    password = passwordEncoder.encryptPassword(association.password),
                    rol = Rol.valueOf(association.rol),
                    description = association.description,
                    url = association.url
                )
                return associationRepository.update(updated)
            } ?: run {
                throw AssociationNotFoundException("No se ha encontrado una asociación con id $idAssociation")
            }
        }
    }

    suspend fun deleteAssociation(id: String): Boolean{
        val find = associationRepository.findById(id)
        find?.let {
            return associationRepository.delete(it)
        }?: run{
            throw AssociationNotFoundException("No se ha encontrado una asociación con id $id")
        }
    }

    suspend fun findAllAssociations(): List<Association>{
        return associationRepository.findAll().toList()
    }

    suspend fun deleteAllAssociations(): Boolean{
        return associationRepository.deleteAll()
    }
}