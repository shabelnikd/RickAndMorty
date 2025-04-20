package com.shabelnikd.rickandmorty.data.models.episodes


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeDto(
    @SerialName("id")
    val id: Int?, // 1
    @SerialName("name")
    val name: String?, // Pilot
    @SerialName("air_date")
    val airDate: String?, // December 2, 2013
    @SerialName("episode")
    val episode: String?, // S01E01
    @SerialName("characters")
    val characters: List<String>?,
    @SerialName("url")
    val url: String?, // https://rickandmortyapi.com/api/episode/1
    @SerialName("created")
    val created: String? // 2017-11-10T12:56:33.798Z
)