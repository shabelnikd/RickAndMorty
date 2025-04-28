package com.shabelnikd.rickandmorty.data.repository.characters

import com.shabelnikd.rickandmorty.data.datasource.local.FavoriteCharacterDao
import com.shabelnikd.rickandmorty.data.datasource.network.api.characters.CharacterApiService
import com.shabelnikd.rickandmorty.data.mappers.toDomain
import com.shabelnikd.rickandmorty.domain.models.characters.CharacterModel
import com.shabelnikd.rickandmorty.domain.models.characters.FavoriteCharacterEntity
import com.shabelnikd.rickandmorty.domain.repository.FavoriteCharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteCharactersRepositoryImpl(
    private val favoriteCharacterDao: FavoriteCharacterDao,
    private val apiService: CharacterApiService,

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


    override suspend fun getFullFavoriteCharacters(ids: List<Int>): Result<List<CharacterModel>> {
        if (ids.isEmpty()) {
            return Result.success(emptyList())
        }

        val apiResult = apiService.getCharactersByIds(ids)

        return apiResult.map { characterList ->
            characterList.map { characterDto ->
                characterDto.toDomain()
            }
        }

    }
}