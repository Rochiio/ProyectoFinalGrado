package com.example.mappers

import com.example.dto.AssociationCreateDto
import com.example.dto.AssociationDto
import com.example.models.users.Association
import com.example.models.users.Rol

fun Association.toAssociationDto(): AssociationDto {
    return AssociationDto(
        uuid = this.uuid,
        name = this.name,
        email = this.email,
        username = this.username,
        rol = this.rol.name,
        description = this.description,
        url = this.url,
        image = this.image
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