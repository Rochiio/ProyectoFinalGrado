package com.example.repositories.users

import com.example.models.users.User
import com.example.repositories.ICrud
import java.util.UUID

interface UserRepository: ICrud<User, String> {
    suspend fun findByEmail(email: String): User?
}