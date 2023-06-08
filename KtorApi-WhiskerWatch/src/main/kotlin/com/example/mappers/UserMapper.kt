package com.example.mappers

import com.example.dto.UserDto
import com.example.dto.UserWithTokenDto
import com.example.models.users.User

/**
 * Mapper para pasar de usuario a usuarioDTO
 */
fun User.toUserDto():UserDto{
    return UserDto(
        id = this.id,
        username = this.username,
        name = this.name,
        email = this.email,
        rol = this.rol.name
    )
}


/**
 * Mapper para pasar de usuario a usuario con token DTO
 */
fun User.toUserWithToken(token:String):UserWithTokenDto{
    return UserWithTokenDto(
        user = this.toUserDto(),
        token = token
    )
}

