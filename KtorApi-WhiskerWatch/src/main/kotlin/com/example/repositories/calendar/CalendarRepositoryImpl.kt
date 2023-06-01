package com.example.repositories.calendar

import com.example.db.MongoDbManager
import com.example.models.calendar.Calendar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mu.KotlinLogging
import org.koin.core.annotation.Single
import org.litote.kmongo.eq

/**
 * Respositiorio calendario
 */
@Single
class CalendarRepositoryImpl: CalendarRepository {
    private val logger = KotlinLogging.logger{}
    private val dbMongo = MongoDbManager.database


    /**
     * Buscar calendario por el id del mapa asociado
     * @param id id por el que buscar
     * @return calendario si se ha encontrado o null
     */
    override suspend fun findByMapsId(id: String): Calendar? {
        logger.info { "Buscando calendario por el id del mapa" }
        return dbMongo.getCollection<Calendar>()
            .findOne(Calendar::mapsId eq id)
    }


    /**
     * Buscar calendario por el id
     * @param id id por el que buscar
     * @return calendario si se ha encontrado o null
     */
    override suspend fun findById(id: String): Calendar? {
        logger.info { "Buscando calendario por ID" }
        return dbMongo.getCollection<Calendar>()
            .findOneById(id)
    }


    /**
     * Salvar calendario
     * @param item calendario a salvar
     * @return calendario salvado
     */
    override suspend fun save(item: Calendar): Calendar {
        logger.info { "Guardando calendario" }
        return dbMongo.getCollection<Calendar>()
            .save(item).let { item }
    }


    /**
     * Actualizar calendario
     * @param item calendario a actualizar
     * @return calendario actualizado
     */
    override suspend fun update(item: Calendar): Calendar {
        logger.info { "Actualizando calendario" }
        return dbMongo.getCollection<Calendar>()
            .updateOneById(item.id, item)
            .wasAcknowledged()
            .let{item}
    }


    /**
     * Eliminar calendario
     * @param item calendario a eliminar
     * @return si se ha eliminado correctamente
     */
    override suspend fun delete(item: Calendar): Boolean {
        logger.info { "Eliminando calendario" }
        return dbMongo.getCollection<Calendar>()
            .deleteOneById(item.id)
            .wasAcknowledged()
    }


    /**
     * Buscar todos los calendarios almacenados
     * @return flujo de calendarios
     */
    override suspend fun findAll(): Flow<Calendar> {
        logger.info { "Buscando todos los calendarios" }
        return dbMongo.getCollection<Calendar>()
            .find().publisher.asFlow()
    }


    /**
     * Eliminar todos los calendarios
     * @return si se han eliminado correctamente
     */
    override suspend fun deleteAll(): Boolean {
        logger.info { "Eliminando todos los calendarios" }
        return dbMongo.getCollection<Calendar>()
            .deleteMany("{}")
            .wasAcknowledged()
    }

}