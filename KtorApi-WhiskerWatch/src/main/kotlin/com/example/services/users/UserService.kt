package com.example.services.users

import com.example.dto.UserCreateDto
import com.example.error.UserError
import com.example.models.users.Rol
import com.example.models.users.User
import com.example.repositories.users.UserRepositoryImpl
import com.example.services.password.BcryptService
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single

@Single
class UserService(
    private val userRepository: UserRepositoryImpl,
    private val passwordEncoder: BcryptService
){

    /**
     * Buscar un usuario por el email.
     * @param email email por el que buscar.
     * @throws UserNotFoundError si no se encuentra ningun usuario con ese email.
     * @return el usuario encontrado.
     */
    suspend fun findUserByEmail(email: String): Result<User, UserError>{
        return userRepository.findByEmail(email)?.let {
            Ok(it)
        }?: run{
            Err(UserError.UserNotFoundError("No se ha encontrado un usuario con email $email"))
        }
    }

    /**
     * Buscar un usuario por el uuid.
     * @param id id por el que buscar.
     * @throws UserNotFoundError siu no se encuentra ningun usuario con ese UUID.
     * @return el usuario encontrado.
     */
    suspend fun findUserById(id: String): Result<User, UserError> {
        return userRepository.findById(id)?.let {
            Ok(it)
        }?: run{
            Err(UserError.UserNotFoundError("No se ha encontrado un usuario con id $id"))
        }
    }

    /**
     * Guardar un usuario.
     * @param user Usuario a añadir.
     * @throws UserBadRequestError si ya existe un usuario con el email.
     * @return el usuario guardado.
     */
    suspend fun saveUser(user: UserCreateDto): Result<User, UserError> {
        return userRepository.findByEmail(user.email)?.let {
            Err(UserError.UserBadRequestError("Ya existe un usuario con email ${it.email}"))
        }?: run {
            val save = User(
                name = user.name, email = user.email, password = passwordEncoder.encryptPassword(user.password),
                username = user.username, rol = Rol.valueOf(user.rol)
            )
            Ok(userRepository.save(save))
        }
    }

    /**
     * Actualizar usuario.
     * @param user datos del usuario a actualizar.
     * @param idUser usuario a actualizar.
     * @throws UserNotFoundError no se encuentra usuario.
     * @throws UserBadRequestError ya existe un usuario con ese email.
     * @return usuario actualizado.
     */
    suspend fun updateUser(user: UserCreateDto, idUser: String): Result<User, UserError>{
        return userRepository.findByEmail(user.email)?.let{
            Err(UserError.UserBadRequestError("Ya existe un usuario con email ${it.email}"))
        }?: run {
            userRepository.findById(idUser)?.let {
                val update = User(
                    id = it.id,
                    name = user.name,
                    email = user.email,
                    password = passwordEncoder.encryptPassword(user.password),
                    username = user.username,
                    rol = Rol.valueOf(user.rol)
                )
                Ok(userRepository.update(update))
            } ?: run {
                Err(UserError.UserNotFoundError("No se ha encontrado un usuario con el ID $idUser"))
            }
        }
    }

    /**
     * Eliminar un usuario.
     * @param id id del usuario a eliminar.
     * @throws UserNotFoundError si no se encuentra el usuario.
     * @return si se ha eliminado correctamente el usuario.
     */
    suspend fun deleteUser(id: String): Result<Boolean, UserError>{
        return userRepository.findById(id)?.let {
            Ok(userRepository.delete(it))
        }?: run{
            Err(UserError.UserNotFoundError("No se ha encontrado un usario con el ID $id"))
        }
    }

    /**
     * Buscar todos los usuarios.
     * @return lista con todos los usuarios.
     */
    suspend fun findAllUsers(): List<User>{
        return userRepository.findAll().toList()
    }

    /**
     * Eliminando todos los usuarios.
     * @return si se ha eliminado correctamente todos los usuarios.
     */
    suspend fun deleteAllUsers(): Boolean{
        return userRepository.deleteAll()
    }
}