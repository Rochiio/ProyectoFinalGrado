package com.example.repositories.calendar

import com.example.db.MongoDbManager
import com.example.models.calendar.Calendar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mu.KotlinLogging
import org.koin.core.annotation.Single
import org.litote.kmongo.eq
import java.util.*

@Single
class CalendarRepositoryImpl: CalendarRepository {
    private val logger = KotlinLogging.logger{}
    private val dbMongo = MongoDbManager.database

    override suspend fun findByMapsUuid(uuid: UUID): Calendar? {
        logger.info { "Buscando calendario por el uuid del mapa" }
        return dbMongo.getCollection<Calendar>()
            .findOne(Calendar::mapsUUID eq uuid)
    }

    override suspend fun findByUUID(uuid: UUID): Calendar? {
        logger.info { "Buscando calendario por UUID" }
        return dbMongo.getCollection<Calendar>()
            .findOne(Calendar::uuid eq uuid)
    }

    override suspend fun save(item: Calendar): Calendar {
        logger.info { "Guardando calendario" }
        return dbMongo.getCollection<Calendar>()
            .save(item).let { item }
    }

    override suspend fun update(item: Calendar): Calendar {
        logger.info { "Actualizando calendario" }
        return dbMongo.getCollection<Calendar>()
            .updateOneById(item.id, item)
            .wasAcknowledged()
            .let{item}
    }

    override suspend fun delete(item: Calendar): Boolean {
        logger.info { "Eliminando calendario" }
        return dbMongo.getCollection<Calendar>()
            .deleteOneById(item.id)
            .wasAcknowledged()
    }

    override suspend fun findAll(): Flow<Calendar> {
        logger.info { "Buscando todos los calendarios" }
        return dbMongo.getCollection<Calendar>()
            .find().publisher.asFlow()
    }

    override suspend fun deleteAll(): Boolean {
        logger.info { "Eliminando todos los calendarios" }
        return dbMongo.getCollection<Calendar>()
            .deleteMany("{}")
            .wasAcknowledged()
    }
}