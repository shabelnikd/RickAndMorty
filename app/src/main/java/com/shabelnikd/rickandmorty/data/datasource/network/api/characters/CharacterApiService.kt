package com.shabelnikd.rickandmorty.data.datasource.network.api.characters


import androidx.compose.ui.util.fastJoinToString
import com.shabelnikd.rickandmorty.data.core.utils.makeRequest
import com.shabelnikd.rickandmorty.data.datasource.network.api.BASE_URL
import com.shabelnikd.rickandmorty.data.mappers.toDomain
import com.shabelnikd.rickandmorty.data.models.characters.CharacterDto
import com.shabelnikd.rickandmorty.data.models.characters.CharacterResponseDto
import com.shabelnikd.rickandmorty.domain.models.characters.Info
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.HttpMethod


class CharacterApiService(
    private val httpClient: HttpClient,
) {

    suspend fun getCharacters(
        page: Int,
        query: String? = null,
        status: String? = null,
        gender: String? = null,
        species: String? = null,
        type: String? = null
    ): Result<CharacterResponseDto> = httpClient.makeRequest {
        url("${BASE_URL}/character")
        method = HttpMethod.Companion.Get
        parameter("page", page)

        query.takeUnless { it.isNullOrBlank() }?.let {
            parameter("name", it)
        }
        status.takeUnless { it.isNullOrBlank() }?.let {
            parameter("status", it)
        }
        gender.takeUnless { it.isNullOrBlank() }?.let {
            parameter("gender", it)
        }
        species.takeUnless { it.isNullOrBlank() }?.let {
            parameter("species", it)
        }
        type.takeUnless { it.isNullOrBlank() }?.let {
            parameter("type", it)
        }

    }


    suspend fun getCharacterInfoOnly(): Result<Info> {
        val responseResult: Result<CharacterResponseDto> = httpClient.makeRequest {
            url("${BASE_URL}/character")
            method = HttpMethod.Get
            parameter("page", 1)
        }

        return responseResult.fold(
            onSuccess = { characterResponse ->
                characterResponse.info?.let { info ->
                    Result.success(info.toDomain())
                } ?: run {
                    Result.failure(IllegalStateException("Объект Info отсутствует в ответе API для первой страницы"))
                }
            },
            onFailure = { throwable ->
                Result.failure(throwable)
            }
        )
    }

    suspend fun getCharacterById(characterId: Int): Result<CharacterDto> = httpClient.makeRequest {
        url("${BASE_URL}/character/${characterId}")
        method = HttpMethod.Companion.Get
    }

    suspend fun getCharactersByIds(charactersIds: List<Int>): Result<List<CharacterDto>> =
        httpClient.makeRequest {
            url(
                "${BASE_URL}/character/${
                    charactersIds.fastJoinToString(
                        separator = ",", prefix = "[", postfix = "]"
                    )
                }"
            )
            method = HttpMethod.Companion.Get
        }

}