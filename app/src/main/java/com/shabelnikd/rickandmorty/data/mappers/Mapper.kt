package com.shabelnikd.rickandmorty.data.mappers

import com.shabelnikd.rickandmorty.data.models.characters.CharacterDto
import com.shabelnikd.rickandmorty.data.models.characters.LocationDto
import com.shabelnikd.rickandmorty.data.models.characters.OriginDto
import com.shabelnikd.rickandmorty.domain.models.characters.Location
import com.shabelnikd.rickandmorty.domain.models.characters.Origin
import com.shabelnikd.rickandmorty.domain.models.characters.Character


fun CharacterDto.toDomain(): Character {
    return Character(
        id = this.id ?: 0,
        name = this.name.orEmpty(),
        status = this.status.orEmpty(),
        species = this.species.orEmpty(),
        type = this.type.orEmpty(),
        gender = this.gender.orEmpty(),
        origin = this.origin?.toDomain() ?: Origin(name = "", url = ""),
        location = this.location?.toDomain() ?: Location(name = "", url = ""),
        image = this.image.orEmpty(),
        episode = this.episode.orEmpty(),
        url = this.url.orEmpty(),
        created = this.created.orEmpty()
    )
}

fun OriginDto.toDomain(): Origin {
    return Origin(
        name = this.name.orEmpty(),
        url = this.url.orEmpty()
    )
}

fun LocationDto.toDomain(): Location {
    return Location(
        name = name.orEmpty(),
        url = url.orEmpty()
    )
}