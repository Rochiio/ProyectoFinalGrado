package com.example.services.maps

import com.example.dto.MapsCreateDto
import com.example.error.MapsError
import com.example.models.Maps
import com.example.repositories.maps.MapRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.toList
import org.koin.core.annotation.Single

@Single
class MapsService(
    private val mapRepository: MapRepository
) {

    suspend fun findMapById(id: String): Result<Maps, MapsError>{
        return mapRepository.findById(id)?.let {
            Ok(it)
        }?: run{
            Err(MapsError.MapsNotFoundError("No se ha encontrado un mapa con el id $id"))
        }
    }

    suspend fun saveMap(map: MapsCreateDto): Result<Maps, MapsError>{
        val created = Maps(latitude = map.latitude, longitude = map.longitude)
        return Ok(mapRepository.save(created))
    }

    suspend fun updateMap(map: MapsCreateDto, idMap: String): Result<Maps, MapsError>{
        return mapRepository.findById(idMap)?.let {
            val updated = Maps(id = it.id, latitude = map.latitude, longitude = map.longitude)
            Ok(mapRepository.update(updated))
        }?: run{
            Err(MapsError.MapsNotFoundError("No se ha encontrado un mapa con el id $idMap"))
        }
    }

    suspend fun deleteMap(id: String): Result<Boolean, MapsError>{
        return mapRepository.findById(id)?.let{
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
}