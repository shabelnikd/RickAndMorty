package com.shabelnikd.rickandmorty.data.repository.characters

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.shabelnikd.rickandmorty.data.datasource.network.api.characters.CharacterApiService
import com.shabelnikd.rickandmorty.data.datasource.network.paging.characters.CharacterPageSource
import com.shabelnikd.rickandmorty.data.mappers.toDomain
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class CharactersRepository(
    private val api: CharacterApiService
) {
    fun getCharacters(): Flow<PagingData<Character>> {
        val page = Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CharacterPageSource(api)
            }
        )


        return page.flow.map { pagingData ->
            pagingData.map { characterDto ->
                characterDto.toDomain()
            }
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getCharacterById(characterId: Int): Result<Character> =
        api.getCharacterById(characterId = characterId).map { response ->
            response.toDomain()
        }

}

//    suspend fun getCharactersByIds(charactersIds: List<Int>): Flow<Character> {
//
//    }