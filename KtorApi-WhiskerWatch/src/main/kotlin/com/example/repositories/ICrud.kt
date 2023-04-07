package com.example.repositories

import kotlinx.coroutines.flow.Flow

interface ICrud<T,UUID> {
    suspend fun findByUUID(uuid: UUID): T?
    suspend fun save(item: T): T
    suspend fun update(item: T): T
    suspend fun delete(item: T):Boolean
    suspend fun findAll(): Flow<T>
    suspend fun deleteAll():Boolean
}