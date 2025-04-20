package com.shabelnikd.rickandmorty.data.repository.locations

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.shabelnikd.rickandmorty.data.datasource.network.api.characters.CharacterApiService
import com.shabelnikd.rickandmorty.data.datasource.network.api.locations.LocationApiService
import com.shabelnikd.rickandmorty.data.datasource.network.paging.characters.CharacterPageSource
import com.shabelnikd.rickandmorty.data.datasource.network.paging.locations.LocationPageSource
import com.shabelnikd.rickandmorty.data.mappers.toDomain
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import com.shabelnikd.rickandmorty.domain.models.locations.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class LocationsRepository(
    private val api: LocationApiService
) {
    fun getLocations(): Flow<PagingData<Location>> {
        val page = Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                LocationPageSource(api)
            }
        )

        return page.flow.map { pagingData ->
            pagingData.map { characterDto ->
                characterDto.toDomain()
            }
        }.flowOn(Dispatchers.IO)
    }


    fun getLocationById(locationId: Int): Flow<Result<Location>> =
        flow {
            emit(
                api.getLocationById(locationId = locationId).map { response ->
                    response.toDomain()
                })
        }.flowOn(Dispatchers.IO)
}

//    suspend fun getCharactersByIds(charactersIds: List<Int>): Flow<Character> {
//
//    }