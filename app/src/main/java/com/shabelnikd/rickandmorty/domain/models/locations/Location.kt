package com.shabelnikd.rickandmorty.domain.models.locations


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Location(
    val id: Int, // 1
    val name: String, // Earth (C-137)
    val type: String, // Planet
    val dimension: String, // Dimension C-137
    val residents: List<String>,
    val url: String, // https://rickandmortyapi.com/api/location/1
    val created: String // 2017-11-10T12:42:04.162Z
)