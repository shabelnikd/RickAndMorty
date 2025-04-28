package com.shabelnikd.rickandmorty.domain.models.characters

data class CharacterResponse(
    val info: Info,
    val results: List<CharacterModel>
)