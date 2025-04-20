package com.shabelnikd.rickandmorty.data.datasource.network.paging.characters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shabelnikd.rickandmorty.data.models.characters.CharacterDto
import com.shabelnikd.rickandmorty.data.datasource.network.api.characters.CharacterApiService

const val START_INDEX = 1

class CharacterPageSource(
    private val api: CharacterApiService
) : PagingSource<Int, CharacterDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterDto> {
        try {
            val currentKey = params.key ?: START_INDEX
            val response = api.getCharacters(currentKey)

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

    override fun getRefreshKey(state: PagingState<Int, CharacterDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
