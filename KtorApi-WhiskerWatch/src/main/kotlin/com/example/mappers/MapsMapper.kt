package com.example.mappers

import com.example.dto.MapsDto
import com.example.models.Maps

fun Maps.toMapsDto():MapsDto{
    return MapsDto(
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude
    )
}