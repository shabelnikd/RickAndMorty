package com.shabelnikd.rickandmorty.data.models.locations


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    @SerialName("id")
    val id: Int?, // 1
    @SerialName("name")
    val name: String?, // Earth (C-137)
    @SerialName("type")
    val type: String? = "", // Planet
    @SerialName("dimension")
    val dimension: String?, // Dimension C-137
    @SerialName("residents")
    val residents: List<String>? = null,
    @SerialName("url")
    val url: String?, // https://rickandmortyapi.com/api/location/1
    @SerialName("created")
    val created: String? // 2017-11-10T12:42:04.162Z
)