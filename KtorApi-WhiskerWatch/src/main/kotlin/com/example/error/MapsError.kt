package com.example.error

sealed class MapsError(val message: String){
    class MapsNotFoundError(message: String) : MapsError(message)
    class MapsBadRequestError(message: String) : MapsError(message)
}
