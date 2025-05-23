package com.shabelnikd.rickandmorty.domain.models.characters

data class CharacterModel(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val characterLocation: CharacterLocation,
    val image: String,
    val episode: List<String?>,
    val url: String,
    val created: String
)