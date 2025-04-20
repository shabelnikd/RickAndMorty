package com.shabelnikd.rickandmorty.data.datasource.network.api.characters


import com.shabelnikd.rickandmorty.data.core.utils.makeRequest
import com.shabelnikd.rickandmorty.data.datasource.network.api.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import com.shabelnikd.rickandmorty.data.models.characters.BaseResponseDto


class CharacterApiService(
    private val httpClient: HttpClient,
) {

    suspend fun getCharacters(page: Int): Result<BaseResponseDto> =
        httpClient.makeRequest({
            url("${BASE_URL}/character")
            method = HttpMethod.Companion.Get
            parameter("page", page)
        })

}