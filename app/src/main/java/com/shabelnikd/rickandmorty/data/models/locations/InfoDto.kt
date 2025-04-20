package com.shabelnikd.rickandmorty.data.models.locations


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InfoDto(
    @SerialName("count")
    val count: Int?, // 126
    @SerialName("pages")
    val pages: Int?, // 7
    @SerialName("next")
    val next: String?, // https://rickandmortyapi.com/api/location?page=2
    @SerialName("prev")
    val prev: String? // null
)