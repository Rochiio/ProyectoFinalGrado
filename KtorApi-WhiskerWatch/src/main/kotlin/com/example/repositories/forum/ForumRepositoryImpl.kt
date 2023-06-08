package com.example.repositories.forum

import com.example.db.MongoDbManager
import com.example.models.forum.Forum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mu.KotlinLogging
import org.koin.core.annotation.Single
import org.litote.kmongo.eq

/**
 * Repositorio foro
 */
@Single
class ForumRepositoryImpl: ForumRepository {
    private val logger = KotlinLogging.logger{}
    private val dbMongo = MongoDbManager.database


    /**
     * Buscar foro por el id del mapa asociado
     * @param id id por el que buscar
     * @return foro si se ha encontrado o null
     */
    override suspend fun findByMapsId(id: String): Forum? {
        logger.info { "Buscando foro por el id del mapa" }
        return dbMongo.getCollection<Forum>()
            .findOne(Forum::mapsId eq id)
    }


    /**
     * Buscar foro por id
     * @param id id por el que buscar
     * @return foro si se ha encontrado o null
     */
    override suspend fun findById(id: String): Forum? {
        logger.info { "Buscando foro por ID" }
        return dbMongo.getCollection<Forum>()
            .findOneById(id)
    }


    /**
     * Salvar foro
     * @param item foro a salvar
     * @return foro salvado
     */
    override suspend fun save(item: Forum): Forum {
        logger.info { "Guardando foro"}
        return dbMongo.getCollection<Forum>()
            .save(item).let { item }
    }


    /**
     * Actualizar foro
     * @param item foro a actualizar
     * @return foro actualizado
     */
    override suspend fun update(item: Forum): Forum {
        logger.info { "Actualizando foro"}
        return dbMongo.getCollection<Forum>()
            .updateOneById(item.id, item)
            .wasAcknowledged().let { item }
    }


    /**
     * Eliminar foro
     * @param item foro a eliminar
     * @return si se ha eliminado correctamente
     */
    override suspend fun delete(item: Forum): Boolean {
        logger.info { "Eliminando foro" }
        return dbMongo.getCollection<Forum>()
            .deleteOneById(item.id)
            .wasAcknowledged()
    }


    /**
     * Buscar todos los foros almacenados
     * @return flujo de foros
     */
    override suspend fun findAll(): Flow<Forum> {
        logger.info { "Buscando todos los foros"}
        return dbMongo.getCollection<Forum>()
            .find().publisher.asFlow()
    }


    /**
     * Eliminar todos los foros
     * @return si se han eliminado correctamente
     */
    override suspend fun deleteAll(): Boolean {
        logger.info{"Eliminando todos los foros"}
        return dbMongo.getCollection<Forum>()
            .deleteMany("{}")
            .wasAcknowledged()
    }
}