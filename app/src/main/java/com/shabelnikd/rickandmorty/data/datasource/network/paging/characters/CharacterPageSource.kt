package com.shabelnikd.rickandmorty.data.datasource.network.paging.characters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shabelnikd.rickandmorty.data.datasource.network.api.characters.CharacterApiService
import com.shabelnikd.rickandmorty.data.models.characters.CharacterDto
import com.shabelnikd.rickandmorty.data.models.characters.CharacterResponseDto

const val START_INDEX = 1

class CharacterPageSource(
    private val api: CharacterApiService,
    private val query: String?
) : PagingSource<Int, CharacterDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterDto> {
        val currentKey = params.key ?: START_INDEX

        return try {
            val response: Result<CharacterResponseDto> = api.getCharacters(currentKey, query)

            response.fold(
                onSuccess = { data ->
                    LoadResult.Page(
                        data = data.results.orEmpty(),
                        prevKey = data.info?.prev?.let { currentKey.minus(1) },
                        nextKey = data.info?.next?.let { currentKey.plus(1) }
                    )
                },
                onFailure = { throwable ->
                    LoadResult.Error(throwable = throwable)
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
