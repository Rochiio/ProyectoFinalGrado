package com.example.repositories.forum

import com.example.db.MongoDbManager
import com.example.models.forum.Forum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mu.KotlinLogging
import org.koin.core.annotation.Single
import org.litote.kmongo.eq
import java.util.*

@Single
class ForumRepositoryImpl: ForumRepository {
    private val logger = KotlinLogging.logger{}
    private val dbMongo = MongoDbManager.database

    override suspend fun findByMapsUuid(uuid: String): Forum? {
        logger.info { "Buscando foro por el uuid del mapa" }
        return dbMongo.getCollection<Forum>()
            .findOne(Forum::mapsUuid eq uuid)
    }

    override suspend fun findByUUID(uuid: String): Forum? {
        logger.info { "Buscando foro por UUID" }
        return dbMongo.getCollection<Forum>()
            .findOne(Forum::uuid eq uuid)
    }

    override suspend fun save(item: Forum): Forum {
        logger.info { "Guardando foro"}
        return dbMongo.getCollection<Forum>()
            .save(item).let { item }
    }

    override suspend fun update(item: Forum): Forum {
        logger.info { "Actualizando foro"}
        return dbMongo.getCollection<Forum>()
            .updateOneById(item.id, item)
            .wasAcknowledged().let { item }
    }

    override suspend fun delete(item: Forum): Boolean {
        logger.info { "Eliminando foro" }
        return dbMongo.getCollection<Forum>()
            .deleteOneById(item.id)
            .wasAcknowledged()
    }

    override suspend fun findAll(): Flow<Forum> {
        logger.info { "Buscando todos los foros"}
        return dbMongo.getCollection<Forum>()
            .find().publisher.asFlow()
    }

    override suspend fun deleteAll(): Boolean {
        logger.info{"Eliminando todos los foros"}
        return dbMongo.getCollection<Forum>()
            .deleteMany("{}")
            .wasAcknowledged()
    }
}