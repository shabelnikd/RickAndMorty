package com.shabelnikd.rickandmorty.data.repository.characters

import com.shabelnikd.rickandmorty.data.datasource.local.FavoriteCharacterDao
import com.shabelnikd.rickandmorty.domain.models.characters.FavoriteCharacterEntity
import com.shabelnikd.rickandmorty.domain.repository.FavoriteCharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteCharactersRepositoryImpl(
    private val favoriteCharacterDao: FavoriteCharacterDao,

    ) : FavoriteCharacterRepository {
    override suspend fun addFavoriteCharacter(characterId: Int) {
        val favorite = FavoriteCharacterEntity(characterId = characterId)
        favoriteCharacterDao.insertFavorite(favorite)
    }

    override suspend fun removeFavoriteCharacter(characterId: Int) {
        favoriteCharacterDao.deleteFavoriteById(characterId)

    }

    override fun isCharacterFavorite(characterId: Int): Flow<Boolean> {
        return favoriteCharacterDao.getFavoriteById(characterId).map { it != null }

    }

    override fun getAllFavoriteCharactersIds(): Flow<List<Int>> {
        return favoriteCharacterDao.getAllFavorites()
            .map { favorites -> favorites.map { it.characterId } }
    }
}