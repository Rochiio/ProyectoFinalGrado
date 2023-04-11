package com.example.services.maps

import com.example.dto.MapsCreateDto
import com.example.exception.MapsNotFoundException
import com.example.models.Maps
import com.example.repositories.maps.MapRepository
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single
import java.util.*

@Single
class MapsService(
    private val mapRepository: MapRepository
) {

    suspend fun findMapByUuid(uuid: UUID): Maps{
        return mapRepository.findByUUID(uuid)
            ?: throw MapsNotFoundException("No se ha encontrado un mapa con el uuid $uuid")
    }

    suspend fun saveMap(map: MapsCreateDto): Maps{
        val created = Maps(latitude = map.latitude, longitude = map.longitude)
        return mapRepository.save(created)
    }

    suspend fun updateMap(map: MapsCreateDto, uuidMap: UUID): Maps{
        val find = mapRepository.findByUUID(uuidMap)
        find?.let {
            val updated = Maps(id = it.id, uuid = it.uuid, latitude = map.latitude, longitude = map.longitude)
            return mapRepository.update(updated)
        }?: run{
            throw MapsNotFoundException("No se ha encontrado un mapa con el uuid $uuidMap")
        }
    }

    suspend fun deleteMap(uuid: UUID): Boolean{
        val find = mapRepository.findByUUID(uuid)
        find?.let{
            return mapRepository.delete(it)
        }?: run{
            throw MapsNotFoundException("No se ha encontrado un mapa con el uuid $uuid")
        }
    }

    suspend fun findAllMaps(): List<Maps>{
        return mapRepository.findAll().toList()
    }

    suspend fun deleteAllMaps(): Boolean{
        return mapRepository.deleteAll()
    }
}