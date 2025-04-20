package com.shabelnikd.rickandmorty.domain.models.episodes


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Episode(

    val id: Int, // 1
    val name: String, // Pilot
    val airDate: String, // December 2, 2013
    val episode: String, // S01E01
    val characters: List<String>,
    val url: String, // https://rickandmortyapi.com/api/episode/1
    val created: String // 2017-11-10T12:56:33.798Z
)