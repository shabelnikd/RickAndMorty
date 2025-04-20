package com.shabelnikd.rickandmorty.domain.models.locations


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Info(
    val count: Int, // 126
    val pages: Int, // 7
    val next: String, // https://rickandmortyapi.com/api/location?page=2
    val prev: String
)