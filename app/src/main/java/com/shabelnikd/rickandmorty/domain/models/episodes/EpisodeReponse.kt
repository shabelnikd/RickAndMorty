package com.shabelnikd.rickandmorty.domain.models.episodes


data class EpisodeReponse(
    val info: Info,
    val results: List<Episode>
)