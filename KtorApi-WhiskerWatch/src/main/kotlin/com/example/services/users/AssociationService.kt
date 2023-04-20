package com.example.services.users

import com.example.dto.AssociationCreateDto
import com.example.error.AssociationError
import com.example.models.users.Association
import com.example.models.users.Rol
import com.example.repositories.users.AssociationRepository
import com.example.services.password.BcryptService
import com.example.services.storage.StorageServiceImpl
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import io.ktor.http.content.*
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single
import java.io.File

@Single
class AssociationService(
    private val associationRepository: AssociationRepository,
    private val storageService: StorageServiceImpl,
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
            Err(AssociationError.AssociationFoundError("Ya existe una asociación con email ${it.email}"))
        }?: run{
            val created = Association(
                name = association.name, email = association.email, username = association.username,
                password = passwordEncoder.encryptPassword(association.password), rol = Rol.valueOf(association.rol),
                description = association.description, url = association.url
            )
            Ok(associationRepository.save(created))
        }
    }

    suspend fun updateAssociation(association: AssociationCreateDto, idAssociation: String): Result<Association, AssociationError> {
        associationRepository.findById(idAssociation)?.let {
            val find = associationRepository.findByEmail(association.email)
            if ((find != null && find.id == idAssociation) || (find == null)) {
                val update = Association(
                    id = it.id,
                    name = association.name,
                    email = association.email,
                    username = association.username,
                    password = passwordEncoder.encryptPassword(association.password),
                    rol = Rol.valueOf(association.rol),
                    description = association.description,
                    url = association.url
                )
                return Ok(associationRepository.update(update))
            } else {
                return Err(AssociationError.AssociationFoundError("Ya existe una asociación con email ${association.email}"))
            }
        } ?: run {
            return Err(AssociationError.AssociationNotFoundError("No se ha encontrado una asociación con id $idAssociation"))
        }
    }

    suspend fun deleteAssociation(id: String): Result<Boolean, AssociationError>{
        return associationRepository.findById(id)?.let {
            it.image?.let {img ->
                storageService.deleteFile(img)
            }
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

    private suspend fun updateImage(association: Association, filename: String?){
        val updated = association.copy(image = filename)
        associationRepository.update(updated)
    }

    /**
     * Ficheros
     */
    suspend fun changeImageAssociation(item: PartData.FileItem, id:String): Result<Map<String, String>, AssociationError> {
        return associationRepository.findById(id)?.let {
            it.image?.let { img ->
                storageService.deleteFile(img)
            }
            val result = storageService.saveFile(id, item)
            updateImage(it, result["FileName"])
            Ok(result)
        }?: run{
            Err(AssociationError.AssociationNotFoundError("No se ha encontrado una asociación con id $id"))
        }
    }

    suspend fun deleteImageAssociation(id: String): Result<Boolean, AssociationError>{
        return associationRepository.findById(id)?.let {
            it.image?.let{img ->
                storageService.deleteFile(img)
                updateImage(it, null)
                Ok(true)
            }?: run{
                Err(AssociationError.AssociationNotFoundError("La asociacion con id $id no tiene imagen"))
            }
        }?: run{
            Err(AssociationError.AssociationNotFoundError("No se ha encontrado la asociacion con id $id"))
        }
    }

    suspend fun getImageAssociation(id: String): Result<File, AssociationError>{
        return associationRepository.findById(id)?.let {
            it.image?.let{img ->
                val fichero = storageService.getFile(img)
                fichero?.let {
                    Ok(fichero)
                }?: run{
                  Err(AssociationError.AssociationBadRequestError("Problemas para conseguir la imagen"))
                }
            }?: run{
                Err(AssociationError.AssociationNotFoundError("La asociacion con id $id no tiene imagen"))
            }
        }?: run{
            Err(AssociationError.AssociationNotFoundError("No se ha encontrado la asociacion con id $id"))
        }
    }


}