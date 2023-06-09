package com.example.repositories.users

import com.example.models.users.Association
import com.example.repositories.ICrud
import java.util.*

interface AssociationRepository: ICrud<Association, String> {
    suspend fun findByEmail(email: String): Association?
}