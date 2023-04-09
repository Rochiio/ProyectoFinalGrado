package com.example.services.users

import com.example.dto.UserCreateDto
import com.example.exception.UserNotFoundException
import com.example.models.users.Rol
import com.example.models.users.User
import com.example.repositories.users.UserRepositoryImpl
import com.example.services.password.BcryptService
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single
import java.util.UUID

@Single
class UserService(
    private val userRepository: UserRepositoryImpl,
    private val passwordEncoder: BcryptService
){

    /**
     * Buscar un usuario por el email.
     * @param email email por el que buscar.
     * @throws UserNotFoundException si no se encuentra ningun usuario con ese email.
     * @return el usuario encontrado.
     */
    suspend fun findUserByEmail(email: String): User{
        return userRepository.findByEmail(email)
            ?: throw UserNotFoundException("No se ha encontrado un usuario con email $email")
    }

    /**
     * Buscar un usuario por el uuid.
     * @param uuid uuid por el que buscar.
     * @throws UserNotFoundException siu no se encuentra ningun usuario con ese UUID.
     * @return el usuario encontrado.
     */
    suspend fun findUserByUuid(uuid: UUID): User {
        return userRepository.findByUUID(uuid)
            ?: throw UserNotFoundException("No se ha encontrado un usuario con uuid $uuid")
    }

    /**
     * Guardar un usuario.
     * @param user Usuario a añadir.
     * @return el usuario guardado.
     */
    suspend fun saveUser(user: UserCreateDto): User {
        val save = User(name = user.name, email = user.email, password = passwordEncoder.encryptPassword(user.password),
            username = user.username, rol = Rol.valueOf(user.rol))
        return userRepository.save(save)
    }

    /**
     * Actualizar usuario.
     * @param user datos del usuario a actualizar.
     * @param uuidUser usuario a actualizar.
     * @throws UserNotFoundException no se encuentra usuario.
     * @return usuario actualizado.
     */
    suspend fun updateUser(user: UserCreateDto, uuidUser: UUID): User{
        val find = userRepository.findByUUID(uuidUser)
        find?.let {
            val update = User(id = it.id, uuid = it.uuid, name = user.name, email = user.email,
                password = passwordEncoder.encryptPassword(user.password), username = user.username, rol = Rol.valueOf(user.rol))
            return userRepository.update(update)
        }?: run{
            throw UserNotFoundException("No se ha encontrado un usuario con el UUID $uuidUser")
        }
    }

    /**
     * Eliminar un usuario.
     * @param uuid uuid del usuario a eliminar.
     * @throws UserNotFoundException si no se encuentra el usuario.
     * @return si se ha eliminado correctamente el usuario.
     */
    suspend fun deleteUser(uuid: UUID): Boolean{
        val find = userRepository.findByUUID(uuid)
        find?.let {
            return userRepository.delete(it)
        }?: run{
            throw UserNotFoundException("No se ha encontrado un usario con el UUID $uuid")
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