package com.shabelnikd.rickandmorty.domain.models.episodes


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Info(
    val count: Int,
    val pages: Int,
    val next: String, // https://rickandmortyapi.com/api/episode?page=2
    val prev: String
)