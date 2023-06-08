package com.example.repositories.users

import com.example.db.MongoDbManager
import com.example.models.users.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mu.KotlinLogging
import org.koin.core.annotation.Single
import org.litote.kmongo.eq

/**
 * Repositorio de usuarios
 */
@Single
class UserRepositoryImpl: UserRepository {
    private val logger = KotlinLogging.logger{}
    private val dbMongo = MongoDbManager.database


    /**
     * Buscar un usuario por el email
     * @param email email por el que buscar
     * @return usuario si se ha encontrado o null
     */
    override suspend fun findByEmail(email: String): User? {
        logger.info{"Buscando usuario por email"}
        return dbMongo.getCollection<User>()
            .findOne(User::email eq email)
    }


    /**
     * Buscar un usuario por el id
     * @param id id por el que buscar
     * @return usuario si se ha encontrado o null
     */
    override suspend fun findById(id: String): User? {
        logger.info{"Buscando usuario por ID"}
        return dbMongo.getCollection<User>()
            .findOneById(id)
    }

    /**
     * Salvar usuario
     * @param item usuario a salvar
     * @return usuario salvado
     */
    override suspend fun save(item: User): User {
        logger.info { "Guardando usuario" }
        return dbMongo.getCollection<User>()
            .save(item).let { item }
    }

    /**
     * Actualizar usuario
     * @param item usuario a actualizar
     * @return usuario actualizado
     */
    override suspend fun update(item: User): User {
        logger.info { "Actualizando usuario" }
        return dbMongo.getCollection<User>()
            .updateOneById(item.id, item)
            .wasAcknowledged().let { item }
    }


    /**
     * ELiminar usuario
     * @param item usuario a eliminar
     * @return si se ha eliminado correctamente
     */
    override suspend fun delete(item: User): Boolean {
        logger.info{"Eliminando usuario"}
        return dbMongo.getCollection<User>()
            .deleteOneById(item.id)
            .wasAcknowledged()
    }


    /**
     * Buscar todos los usuarios que hay almacenados
     * @return flujo de usuarios
     */
    override suspend fun findAll(): Flow<User> {
        logger.info { "Buscando todos los usuarios" }
        return dbMongo.getCollection<User>()
            .find().publisher.asFlow()
    }


    /**
     * Eliminar todos los usuarios
     * @return si se han eliminado correctamente o no
     */
    override suspend fun deleteAll(): Boolean {
        logger.info{"Eliminando todos los usuarios"}
        return dbMongo.getCollection<User>()
            .deleteMany("{}")
            .wasAcknowledged()
    }
}