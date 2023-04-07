package com.example.repositories.calendar

import com.example.db.MongoDbManager
import com.example.models.calendar.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mu.KotlinLogging
import org.koin.core.annotation.Single
import org.litote.kmongo.eq
import java.util.*

@Single
class TaskRepositoryImpl: TaskRepository {
    private val logger = KotlinLogging.logger{}
    private val dbMongo = MongoDbManager.database

    override suspend fun findByUUID(uuid: UUID): Task? {
        logger.info { "Buscando tarea por UUID" }
        return dbMongo.getCollection<Task>()
            .findOne(Task::uuid eq uuid)
    }

    override suspend fun save(item: Task): Task {
        logger.info { "Guardando tarea"}
        return dbMongo.getCollection<Task>()
            .save(item).let { item }
    }

    override suspend fun update(item: Task): Task {
        logger.info {"Actualizando tarea"}
        return dbMongo.getCollection<Task>()
            .updateOneById(item.id, item)
            .wasAcknowledged()
            .let { item }
    }

    override suspend fun delete(item: Task): Boolean {
        logger.info {"Eliminando tarea"}
        return dbMongo.getCollection<Task>()
            .deleteOneById(item.id)
            .wasAcknowledged()
    }

    override suspend fun findAll(): Flow<Task> {
        logger.info { "Buscando todas las tareas" }
        return dbMongo.getCollection<Task>()
            .find().publisher.asFlow()
    }

    override suspend fun deleteAll(): Boolean {
        logger.info{"Eliminando todas las tareas"}
        return dbMongo.getCollection<Task>()
            .deleteMany("{}")
            .wasAcknowledged()
    }
}