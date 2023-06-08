package com.example.repositories.users

import com.example.db.MongoDbManager
import com.example.models.users.Association
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mu.KotlinLogging
import org.koin.core.annotation.Single
import org.litote.kmongo.eq

/**
 * Repositiorio de asociaciones
 */
@Single
class AssociationRepositoryImpl: AssociationRepository {
    private val logger = KotlinLogging.logger{}
    private val dbMongo = MongoDbManager.database


    /**
     * Buscar una asociacion por email
     * @param email email por el que buscar
     * @return asociacion si la encuentra o null
     */
    override suspend fun findByEmail(email: String): Association? {
        logger.info { "Buscando asociación por email" }
        return dbMongo.getCollection<Association>()
            .findOne(Association::email eq email)
    }


    /**
     * Buscar una asociaicon por id
     * @param id id por el que buscar
     * @return asociacion si la encuentra o null
     */
    override suspend fun findById(id: String): Association? {
        logger.info { "Buscando asociación por ID" }
        return dbMongo.getCollection<Association>()
            .findOneById(id)
    }


    /**
     * Salvar una asociacion
     * @param item asociacion a salvar
     * @return la asociacion salvada
     */
    override suspend fun save(item: Association): Association {
        logger.info { "Salvando asociación"}
        return dbMongo.getCollection<Association>()
            .save(item).let { item }
    }


    /**
     * Actualizar asociacion
     * @param item asociacion a actualizar
     * @return asociacion actualizada
     */
    override suspend fun update(item: Association): Association {
        logger.info { "Actualizando asociacion" }
        return dbMongo.getCollection<Association>()
            .updateOneById(item.id, item).wasAcknowledged().let { item }
    }


    /**
     * Eliminar asociacion
     * @param item asociacion a eliminar
     * @return si se ha eliminado correctamente
     */
    override suspend fun delete(item: Association): Boolean {
        logger.info { "Eliminando asociación" }
        return dbMongo.getCollection<Association>()
            .deleteOneById(item.id).wasAcknowledged()
    }

    /**
     * Buscar todas las asociaciones
     * @return flujo con todas las asociaciones
     */
    override suspend fun findAll(): Flow<Association> {
        logger.info { "Buscando todas las asociaciones" }
        return dbMongo.getCollection<Association>()
            .find().publisher.asFlow()
    }


    /**
     * Eliminar todas las asociaciones
     * @return si se han eliminado correctamente
     */
    override suspend fun deleteAll(): Boolean {
        logger.info { "Eliminando todas las asociaciones" }
        return dbMongo.getCollection<Association>()
            .deleteMany("{}").wasAcknowledged()
    }
}