package com.shabelnikd.rickandmorty.data.models.episodes


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InfoDto(
    @SerialName("count")
    val count: Int?, // 51
    @SerialName("pages")
    val pages: Int?, // 3
    @SerialName("next")
    val next: String?, // https://rickandmortyapi.com/api/episode?page=2
    @SerialName("prev")
    val prev: String?
)