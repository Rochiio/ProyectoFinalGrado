package com.example.services.maps

import com.example.dto.MapsCreateDto
import com.example.exception.MapsNotFoundException
import com.example.models.Maps
import com.example.repositories.maps.MapRepository
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single

@Single
class MapsService(
    private val mapRepository: MapRepository
) {

    suspend fun findMapById(id: String): Maps{
        return mapRepository.findById(id)
            ?: throw MapsNotFoundException("No se ha encontrado un mapa con el id $id")
    }

    suspend fun saveMap(map: MapsCreateDto): Maps{
        val created = Maps(latitude = map.latitude, longitude = map.longitude)
        return mapRepository.save(created)
    }

    suspend fun updateMap(map: MapsCreateDto, idMap: String): Maps{
        val find = mapRepository.findById(idMap)
        find?.let {
            val updated = Maps(id = it.id, latitude = map.latitude, longitude = map.longitude)
            return mapRepository.update(updated)
        }?: run{
            throw MapsNotFoundException("No se ha encontrado un mapa con el id $idMap")
        }
    }

    suspend fun deleteMap(id: String): Boolean{
        val find = mapRepository.findById(id)
        find?.let{
            return mapRepository.delete(it)
        }?: run{
            throw MapsNotFoundException("No se ha encontrado un mapa con el id $id")
        }
    }

    suspend fun findAllMaps(): List<Maps>{
        return mapRepository.findAll().toList()
    }

    suspend fun deleteAllMaps(): Boolean{
        return mapRepository.deleteAll()
    }
}