package com.shabelnikd.rickandmorty.domain.repository

import com.shabelnikd.rickandmorty.domain.models.characters.CharacterModel
import kotlinx.coroutines.flow.Flow

interface FavoriteCharacterRepository {

    suspend fun addFavoriteCharacter(characterId: Int)

    suspend fun removeFavoriteCharacter(characterId: Int)

    fun isCharacterFavorite(characterId: Int): Flow<Boolean>

    fun getAllFavoriteCharactersIds(): Flow<List<Int>>

    suspend fun getFullFavoriteCharacters(ids: List<Int>): Result<List<CharacterModel>>
}