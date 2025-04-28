package com.shabelnikd.rickandmorty.data.mappers


import com.shabelnikd.rickandmorty.data.models.characters.CharacterDto
import com.shabelnikd.rickandmorty.data.models.characters.CharacterLocationDto
import com.shabelnikd.rickandmorty.data.models.characters.OriginDto
import com.shabelnikd.rickandmorty.data.models.episodes.EpisodeDto
import com.shabelnikd.rickandmorty.data.models.locations.LocationDto
import com.shabelnikd.rickandmorty.domain.models.characters.CharacterLocation
import com.shabelnikd.rickandmorty.domain.models.characters.CharacterModel
import com.shabelnikd.rickandmorty.domain.models.characters.Origin
import com.shabelnikd.rickandmorty.domain.models.episodes.Episode
import com.shabelnikd.rickandmorty.domain.models.locations.Location


fun CharacterDto.toDomain(): CharacterModel {
    return CharacterModel(
        id = this.id ?: 0,
        name = this.name.orEmpty(),
        status = this.status.orEmpty(),
        species = this.species.orEmpty(),
        type = this.type.orEmpty(),
        gender = this.gender.orEmpty(),
        origin = this.origin?.toDomain() ?: Origin(name = "", url = ""),
        characterLocation = this.characterLocation?.toDomain() ?: CharacterLocation(
            name = "",
            url = ""
        ),
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

fun CharacterLocationDto.toDomain(): CharacterLocation {
    return CharacterLocation(
        name = name.orEmpty(),
        url = url.orEmpty()
    )
}


fun LocationDto.toDomain(): Location {
    return Location(
        id = this.id ?: 0,
        name = this.name.orEmpty(),
        type = this.type.orEmpty(),
        dimension = this.dimension.orEmpty(),
        residents = this.residents.orEmpty(),
        url = this.url.orEmpty(),
        created = this.created.orEmpty()
    )
}

fun EpisodeDto.toDomain(): Episode {
    return Episode(
        id = this.id ?: 0,
        name = this.name.orEmpty(),
        airDate = this.airDate.orEmpty(),
        episode = this.episode.orEmpty(),
        characters = this.characters.orEmpty(),
        url = this.url.orEmpty(),
        created = this.created.orEmpty()
    )
}