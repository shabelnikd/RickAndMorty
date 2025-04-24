package com.shabelnikd.rickandmorty.domain.models.characters

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_characters")
data class FavoriteCharacterEntity(
    @PrimaryKey val characterId: Int
)