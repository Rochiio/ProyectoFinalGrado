package com.example.repositories.users

import com.example.db.MongoDbManager
import com.example.models.users.Association
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mu.KotlinLogging
import org.koin.core.annotation.Single
import org.litote.kmongo.eq
import java.util.*

@Single
class AssociationRepositoryImpl: AssociationRepository {
    private val logger = KotlinLogging.logger{}
    private val dbMongo = MongoDbManager.database


    override suspend fun findByEmail(email: String): Association? {
        logger.info { "Buscando asociación por email" }
        return dbMongo.getCollection<Association>()
            .findOne(Association::email eq email)
    }

    override suspend fun findByUUID(uuid: UUID): Association? {
        logger.info { "Buscando asociación por UUID" }
        return dbMongo.getCollection<Association>()
            .findOne(Association::uuid eq uuid)
    }

    override suspend fun save(item: Association): Association {
        logger.info { "Salvando asociación"}
        return dbMongo.getCollection<Association>()
            .save(item).let { item }
    }

    override suspend fun update(item: Association): Association {
        logger.info { "Actualizando asociacion" }
        return dbMongo.getCollection<Association>()
            .updateOneById(item.id, item).wasAcknowledged().let { item }
    }

    override suspend fun delete(item: Association): Boolean {
        logger.info { "Eliminando asociación" }
        return dbMongo.getCollection<Association>()
            .deleteOneById(item.id).wasAcknowledged()
    }

    override suspend fun findAll(): Flow<Association> {
        logger.info { "Buscando todas las asociaciones" }
        return dbMongo.getCollection<Association>()
            .find().publisher.asFlow()
    }

    override suspend fun deleteAll(): Boolean {
        logger.info { "Eliminando todas las asociaciones" }
        return dbMongo.getCollection<Association>()
            .deleteMany("{}").wasAcknowledged()
    }
}