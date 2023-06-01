package com.example.repositories.maps

import com.example.db.MongoDbManager
import com.example.models.Maps
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mu.KotlinLogging
import org.koin.core.annotation.Single
import org.litote.kmongo.eq

/**
 * Repositorio de localizaciones
 */
@Single
class MapRepositoryImpl: MapRepository {
    private val logger = KotlinLogging.logger{}
    private val dbMongo = MongoDbManager.database


    /**
     * Buscar localizacion por longitud y latitud
     * @param latitude latitud a buscar.
     * @param longitude longitud a buscar.
     * @return localizacion si se ha encontrado o null
     */
    override suspend fun findMapByLatLon(latitude: String, longitude: String): Maps? {
        logger.info { "Buscando mapa por latitud y longitud" }
        return dbMongo.getCollection<Maps>()
            .findOne(Maps::latitude eq latitude, Maps::longitude eq longitude)
    }


    /**
     * Buscar una localizacion por id
     * @param id id por el que buscar
     * @return localizacion si se ha encontrado o null
     */
    override suspend fun findById(id: String): Maps? {
        logger.info { "Buscando mapa por UUID" }
        return dbMongo.getCollection<Maps>()
            .findOneById(id)
    }


    /**
     * Salvar localizacion
     * @param item localizacion a salvar
     * @return localizacion salvada
     */
    override suspend fun save(item: Maps): Maps {
        logger.info { "Guardando mapa" }
        return dbMongo.getCollection<Maps>()
            .save(item).let { item }
    }


    /**
     * Actualizar localizacion
     * @param item localizacion a actualizar
     * @return localizacion actualizacion
     */
    override suspend fun update(item: Maps): Maps {
        logger.info { "Actualizando mapa" }
        return dbMongo.getCollection<Maps>()
            .updateOneById(item.id, item)
            .wasAcknowledged().let { item }
    }


    /**
     * Eliminar localizacion
     * @param item localizacion a eliminar
     * @return si se ha eliminado correctamente
     */
    override suspend fun delete(item: Maps): Boolean {
        logger.info { "Eliminando mapa" }
        return dbMongo.getCollection<Maps>()
            .deleteOneById(item.id)
            .wasAcknowledged()
    }


    /**
     * Buscar todas las localizaciones almacenadas
     * @return flujo de localizaciones
     */
    override suspend fun findAll(): Flow<Maps> {
        logger.info { "Buscando todos los mapas" }
        return dbMongo.getCollection<Maps>()
            .find().publisher.asFlow()
    }


    /**
     * Eliminar todas las localizaciones
     * @return si se han eliminado correctamente
     */
    override suspend fun deleteAll(): Boolean {
        logger.info { "Eliminando todos los mapas" }
        return dbMongo.getCollection<Maps>()
            .deleteMany("{}").wasAcknowledged()
    }
}