package com.example.services.users

import com.example.dto.AssociationCreateDto
import com.example.error.AssociationError
import com.example.models.users.Association
import com.example.models.users.Rol
import com.example.repositories.users.AssociationRepository
import com.example.services.password.BcryptService
import com.github.michaelbull.result.*
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single

@Single
class AssociationService(
    private val associationRepository: AssociationRepository,
    private val passwordEncoder: BcryptService
) {

    suspend fun findAssociationByEmail(email: String): Result<Association, AssociationError> {
        return associationRepository.findByEmail(email)?.let {
            Ok(it)
        }?: run{
            Err(AssociationError.AssociationNotFoundError("No se ha encontrado una asociación con email $email"))
        }
    }

    suspend fun findAssociationById(id: String): Result<Association, AssociationError>{
        return associationRepository.findById(id)?.let {
            Ok(it)
        }?: run{
            Err(AssociationError.AssociationNotFoundError("No se ha encontrado una asociación con id $id"))
        }
    }

    suspend fun saveAssociation(association: AssociationCreateDto): Result<Association, AssociationError> {
        return associationRepository.findByEmail(association.email)?.let {
            Err(AssociationError.AssociationBadRequestError("Ya existe una asociación con email ${it.email}"))
        }?: run{
            val created = Association(
                name = association.name, email = association.email, username = association.username,
                password = passwordEncoder.encryptPassword(association.password), rol = Rol.valueOf(association.rol),
                description = association.description, url = association.url
            )
            Ok(associationRepository.save(created))
        }
    }

    suspend fun updateAssociation(association: AssociationCreateDto, idAssociation: String): Result<Association, AssociationError>{
        return associationRepository.findByEmail(association.email)?.let {
            Err(AssociationError.AssociationBadRequestError("Ya existe una asociación con email ${association.email}"))
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
                Ok(associationRepository.update(updated))
            } ?: run {
                Err(AssociationError.AssociationNotFoundError("No se ha encontrado una asociación con id $idAssociation"))
            }
        }
    }

    suspend fun deleteAssociation(id: String): Result<Boolean, AssociationError>{
        return associationRepository.findById(id)?.let {
            Ok(associationRepository.delete(it))
        }?: run{
            Err(AssociationError.AssociationNotFoundError("No se ha encontrado una asociación con id $id"))
        }
    }

    suspend fun findAllAssociations(): List<Association>{
        return associationRepository.findAll().toList()
    }

    suspend fun deleteAllAssociations(): Boolean{
        return associationRepository.deleteAll()
    }
}