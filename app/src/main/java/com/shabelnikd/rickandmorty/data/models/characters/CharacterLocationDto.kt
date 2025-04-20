package com.shabelnikd.rickandmorty.data.models.characters

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterLocationDto(
    @SerialName("name")
    val name: String? = null,
    @SerialName("url")
    val url: String? = null
)