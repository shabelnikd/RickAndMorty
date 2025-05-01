package com.shabelnikd.rickandmorty.data.repository.characters

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.shabelnikd.rickandmorty.data.datasource.network.api.characters.CharacterApiService
import com.shabelnikd.rickandmorty.data.datasource.network.paging.characters.CharacterPageSource
import com.shabelnikd.rickandmorty.data.mappers.toDomain
import com.shabelnikd.rickandmorty.domain.models.characters.CharacterModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class CharactersRepository(
    private val api: CharacterApiService
) {
    fun getCharacters(
        query: String?,
        status: String?,
        gender: String?,
        species: String?,
        type: String?
    ): Flow<PagingData<CharacterModel>> {
        val page = Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 40,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CharacterPageSource(api, query, status, gender, species, type)
            }
        )


        return page.flow.map { pagingData ->
            pagingData.map { characterDto ->
                characterDto.toDomain()
            }
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getCharacterById(characterId: Int): Result<CharacterModel> =
        api.getCharacterById(characterId = characterId).map { response ->
            response.toDomain()
        }


}

