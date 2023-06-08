package com.example.repositories.maps

import com.example.models.Maps
import com.example.repositories.ICrud
import java.util.*

interface MapRepository: ICrud<Maps, String> {
    suspend fun findMapByLatLon(latitude: String, longitude: String): Maps?
}