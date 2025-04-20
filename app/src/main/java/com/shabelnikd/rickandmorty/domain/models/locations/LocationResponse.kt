package com.shabelnikd.rickandmorty.domain.models.locations


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class LocationResponse(
    val info: Info,
    val results: List<Location>
)