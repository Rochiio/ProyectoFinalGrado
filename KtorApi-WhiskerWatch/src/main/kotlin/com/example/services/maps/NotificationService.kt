package com.example.services.maps

import com.example.models.AdoptedNotification

interface NotificationService {
    fun addSuscriber(id: Int, suscriber: suspend (AdoptedNotification) -> Unit)
    fun removeSuscriber(id: Int)
}