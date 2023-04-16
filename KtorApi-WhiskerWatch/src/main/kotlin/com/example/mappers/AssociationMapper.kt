package com.example.mappers

import com.example.dto.AssociationCreateDto
import com.example.dto.AssociationDto
import com.example.dto.AssociationTokenDto
import com.example.models.users.Association
import com.example.models.users.Rol

fun Association.toAssociationDto(): AssociationDto {
    return AssociationDto(
        id = this.id,
        name = this.name,
        email = this.email,
        username = this.username,
        rol = this.rol.name,
        description = this.description,
        url = this.url,
        image = this.image
    )

}

fun Association.toAssociationTokenDto(token: String): AssociationTokenDto {
    return AssociationTokenDto(
        association = this.toAssociationDto(),
        token = token
    )
}

fun AssociationCreateDto.toAssociation(): Association{
    return Association(
        name = this.name,
        email = this.email,
        username = this.username,
        password = this.password,
        rol = Rol.valueOf(this.rol),
        description = this.description,
        url = this.url
    )
}