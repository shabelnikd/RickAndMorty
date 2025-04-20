package com.shabelnikd.rickandmorty.data.models.locations


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationResponseDto(
    @SerialName("info")
    val info: InfoDto?,
    @SerialName("results")
    val results: List<LocationDto>?
)