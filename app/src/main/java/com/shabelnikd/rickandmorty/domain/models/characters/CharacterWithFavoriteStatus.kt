package com.shabelnikd.rickandmorty.domain.models.characters

data class CharacterWithFavoriteStatus(
    val characterModel: CharacterModel,
    val isFavorite: Boolean
)