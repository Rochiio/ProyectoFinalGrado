package com.example.repositories.users

import com.example.db.MongoDbManager
import com.example.models.users.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mu.KotlinLogging
import org.koin.core.annotation.Single
import org.litote.kmongo.eq
import java.util.*

@Single
class UserRepositoryImpl: UserRepository {
    private val logger = KotlinLogging.logger{}
    private val dbMongo = MongoDbManager.database

    override suspend fun findByEmail(email: String): User? {
        logger.info{"Buscando usuario por email"}
        return dbMongo.getCollection<User>()
            .findOne(User::email eq email)
    }

    override suspend fun findById(id: String): User? {
        logger.info{"Buscando usuario por ID"}
        return dbMongo.getCollection<User>()
            .findOneById(id)
    }

    override suspend fun save(item: User): User {
        logger.info { "Guardando usuario" }
        return dbMongo.getCollection<User>()
            .save(item).let { item }
    }

    override suspend fun update(item: User): User {
        logger.info { "Actualizando usuario" }
        return dbMongo.getCollection<User>()
            .updateOneById(item.id, item)
            .wasAcknowledged().let { item }
    }

    override suspend fun delete(item: User): Boolean {
        logger.info{"Eliminando usuario"}
        return dbMongo.getCollection<User>()
            .deleteOneById(item.id)
            .wasAcknowledged()
    }

    override suspend fun findAll(): Flow<User> {
        logger.info { "Buscando todos los usuarios" }
        return dbMongo.getCollection<User>()
            .find().publisher.asFlow()
    }

    override suspend fun deleteAll(): Boolean {
        logger.info{"Eliminando todos los usuarios"}
        return dbMongo.getCollection<User>()
            .deleteMany("{}")
            .wasAcknowledged()
    }
}