package com.example.services.maps

import com.example.dto.MapsCreateDto
import com.example.error.MapsError
import com.example.models.AdoptedNotification
import com.example.models.Maps
import com.example.repositories.maps.MapRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.minutes

@Single
class MapsService(
    private val mapRepository: MapRepository,
): NotificationService {
    private val cache = Cache.Builder()
        .maximumCacheSize(100)
        .expireAfterAccess(15.minutes)
        .build<String, Maps>()
    private val suscribers = mutableMapOf<Int, suspend (AdoptedNotification) -> Unit>()

    suspend fun findMapById(id: String): Result<Maps, MapsError>{
        return cache.get(id)?.let {
            Ok(it)
        }?: run {
            mapRepository.findById(id)?.let {
                Ok(it)
            }?: run{
                Err(MapsError.MapsNotFoundError("No se ha encontrado un mapa con el id $id"))
            }
        }
    }

    suspend fun saveMap(map: MapsCreateDto): Result<Maps, MapsError>{
        return mapRepository.findMapByLatLon(map.latitude, map.longitude)?.let {
            Err(MapsError.MapsBadRequestError("Ya existe un mapa con la latitud ${map.latitude} y la longitud ${map.longitude}"))
        }?: run{
            val created = Maps(latitude = map.latitude, longitude = map.longitude)
            cache.put(created.id, created)
            Ok(mapRepository.save(created))
        }
    }

    suspend fun updateMap(map: MapsCreateDto, idMap: String): Result<Maps, MapsError>{
        return mapRepository.findById(idMap)?.let {
            mapRepository.findMapByLatLon(map.latitude, map.longitude)?.let {
                Err(MapsError.MapsBadRequestError("Ya existe un mapa con la latitud ${map.latitude} y la longitud ${map.longitude}"))
            }?: run {
                val updated = Maps(id = it.id, latitude = map.latitude, longitude = map.longitude)
                cache.put(idMap, updated)
                Ok(mapRepository.update(updated))
            }
        }?: run{
            Err(MapsError.MapsNotFoundError("No se ha encontrado un mapa con el id $idMap"))
        }
    }

    suspend fun deleteMap(id: String): Result<Boolean, MapsError>{
        return mapRepository.findById(id)?.let{
            cache.invalidate(id)
            Ok(mapRepository.delete(it))
        }?: run{
            Err(MapsError.MapsNotFoundError("No se ha encontrado un mapa con el id $id"))
        }
    }

    suspend fun findAllMaps(): List<Maps>{
        return mapRepository.findAll().toList()
    }

    suspend fun deleteAllMaps(): Boolean{
        return mapRepository.deleteAll()
    }

    suspend fun deleteMapsAdoption(id: String): Result<Boolean, MapsError>{
        return mapRepository.findById(id)?.let{
            cache.invalidate(id)
            onChange(it)
            Ok(mapRepository.delete(it))
        }?: run{
            Err(MapsError.MapsNotFoundError("No se ha encontrado un mapa con el id $id"))
        }
    }

    //WebSocket
    override fun addSuscriber(id: Int, suscriber: suspend (AdoptedNotification) -> Unit) {
        suscribers[id] = suscriber
    }

    override fun removeSuscriber(id: Int) {
        suscribers.remove(id)
    }

    private suspend fun onChange(data: Maps) {
        suscribers.values.forEach {
            it.invoke(
                AdoptedNotification(
                    map = data,
                )
            )
        }
    }
}