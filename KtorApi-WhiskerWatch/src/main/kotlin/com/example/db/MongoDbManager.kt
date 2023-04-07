package com.example.db

import mu.KotlinLogging
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.util.Properties


private val logger = KotlinLogging.logger {  }

object MongoDbManager {
    private var mongoClient: CoroutineClient
    var database: CoroutineDatabase

    private val properties = Properties()

    init {

        logger.info("Inicializando conexion a MongoDB")
        properties.load(javaClass.classLoader.getResourceAsStream("mongo.properties"))
        println("Inicializando conexion a MongoDB -> ${properties.getProperty("mongo.conection.local")}")

        mongoClient = KMongo.createClient(properties.getProperty("mongo.conection.local").toString()).coroutine

        database = mongoClient.getDatabase("whiskerwatchdb")
    }
}