package com.shabelnikd.rickandmorty.domain.models.episodes


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class EpisodeReponse(
    val info: Info,
    val results: List<Episode>
)