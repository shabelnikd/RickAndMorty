package com.shabelnikd.rickandmorty.data.datasource.network.paging.locations

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shabelnikd.rickandmorty.data.datasource.network.api.locations.LocationApiService
import com.shabelnikd.rickandmorty.data.models.locations.LocationDto


const val START_INDEX = 1

class LocationPageSource(
    private val api: LocationApiService
) : PagingSource<Int, LocationDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationDto> {
        try {
            val currentKey = params.key ?: START_INDEX
            val response = api.getLocations(currentKey)

            response.onSuccess { data ->
                return LoadResult.Page(
                    data = data.results.orEmpty(),
                    prevKey = if (currentKey == START_INDEX) null else currentKey.minus(1),
                    nextKey = data.info?.next?.let { currentKey.plus(1) }
                )
            }.onFailure { e ->
                return LoadResult.Error(
                    throwable = e
                )
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
        return LoadResult.Invalid()
    }

    override fun getRefreshKey(state: PagingState<Int, LocationDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
