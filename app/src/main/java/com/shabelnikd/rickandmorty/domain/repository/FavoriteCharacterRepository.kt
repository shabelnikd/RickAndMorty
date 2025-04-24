package com.shabelnikd.rickandmorty.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoriteCharacterRepository {

    suspend fun addFavoriteCharacter(characterId: Int)

    suspend fun removeFavoriteCharacter(characterId: Int)

    fun isCharacterFavorite(characterId: Int): Flow<Boolean>
    
    fun getAllFavoriteCharactersIds(): Flow<List<Int>>
}