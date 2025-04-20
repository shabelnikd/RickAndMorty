package com.shabelnikd.rickandmorty.data.models.episodes


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeReponseDto(
    @SerialName("info")
    val info: InfoDto?,
    @SerialName("results")
    val results: List<EpisodeDto>?
)