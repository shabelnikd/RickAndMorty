package com.shabelnikd.rickandmorty.data.repository.edisodes

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.shabelnikd.rickandmorty.data.datasource.network.api.characters.CharacterApiService
import com.shabelnikd.rickandmorty.data.datasource.network.api.episodes.EpisodeApiService
import com.shabelnikd.rickandmorty.data.datasource.network.paging.characters.CharacterPageSource
import com.shabelnikd.rickandmorty.data.datasource.network.paging.edisodes.EpisodePageSource
import com.shabelnikd.rickandmorty.data.mappers.toDomain
import com.shabelnikd.rickandmorty.data.models.episodes.EpisodeDto
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import com.shabelnikd.rickandmorty.domain.models.episodes.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class EpisodesRepository(
    private val api: EpisodeApiService
) {
    fun getEpisodes(): Flow<PagingData<Episode>> {
        val page = Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                EpisodePageSource(api)
            }
        )

        return page.flow.map { pagingData ->
            pagingData.map { episodeDto ->
                episodeDto.toDomain()
            }
        }.flowOn(Dispatchers.IO)
    }


    fun getEpisodeById(episodeId: Int): Flow<Result<Episode>> =
        flow {
            emit(
                api.getEpisodeById(episodeId = episodeId).map { response ->
                    response.toDomain()
                })
        }.flowOn(Dispatchers.IO)
}

//    suspend fun getCharactersByIds(charactersIds: List<Int>): Flow<Character> {
//
//    }