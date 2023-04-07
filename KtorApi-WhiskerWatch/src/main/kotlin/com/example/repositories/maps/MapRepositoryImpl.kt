package com.example.repositories.maps

import com.example.db.MongoDbManager
import com.example.models.Maps
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mu.KotlinLogging
import org.koin.core.annotation.Single
import org.litote.kmongo.eq
import java.util.*

@Single
class MapRepositoryImpl: MapRepository {
    private val logger = KotlinLogging.logger{}
    private val dbMongo = MongoDbManager.database

    override suspend fun findByUUID(uuid: UUID): Maps? {
        logger.info { "Buscando mapa por UUID" }
        return dbMongo.getCollection<Maps>()
            .findOne(Maps::uuid eq uuid)
    }

    override suspend fun save(item: Maps): Maps {
        logger.info { "Guardando mapa" }
        return dbMongo.getCollection<Maps>()
            .save(item).let { item }
    }

    override suspend fun update(item: Maps): Maps {
        logger.info { "Actualizando mapa" }
        return dbMongo.getCollection<Maps>()
            .updateOneById(item.id, item)
            .wasAcknowledged().let { item }
    }

    override suspend fun delete(item: Maps): Boolean {
        logger.info { "Eliminando mapa" }
        return dbMongo.getCollection<Maps>()
            .deleteOneById(item.id)
            .wasAcknowledged()
    }

    override suspend fun findAll(): Flow<Maps> {
        logger.info { "Buscando todos los mapas" }
        return dbMongo.getCollection<Maps>()
            .find().publisher.asFlow()
    }

    override suspend fun deleteAll(): Boolean {
        logger.info { "Eliminando todos los mapas" }
        return dbMongo.getCollection<Maps>()
            .deleteMany("{}").wasAcknowledged()
    }
}