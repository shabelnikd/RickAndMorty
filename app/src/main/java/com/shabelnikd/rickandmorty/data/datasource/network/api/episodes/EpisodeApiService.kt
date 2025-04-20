package com.shabelnikd.rickandmorty.data.datasource.network.api.episodes


import androidx.compose.ui.util.fastJoinToString
import com.shabelnikd.rickandmorty.data.core.utils.makeRequest
import com.shabelnikd.rickandmorty.data.datasource.network.api.BASE_URL
import com.shabelnikd.rickandmorty.data.models.characters.CharacterDto
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import com.shabelnikd.rickandmorty.data.models.characters.CharacterResponseDto
import com.shabelnikd.rickandmorty.data.models.episodes.EpisodeDto
import com.shabelnikd.rickandmorty.data.models.episodes.EpisodeReponseDto


class EpisodeApiService(
    private val httpClient: HttpClient,
) {

    suspend fun getEpisodes(page: Int): Result<EpisodeReponseDto> =
        httpClient.makeRequest {
            url("${BASE_URL}/episode")
            method = HttpMethod.Companion.Get
            parameter("page", page)
        }

    suspend fun getEpisodeById(episodeId: Int): Result<EpisodeDto> =
        httpClient.makeRequest {
            url("${BASE_URL}/episode/${episodeId}")
            method = HttpMethod.Companion.Get
        }

    suspend fun getEpisodesByIds(episodesIds: List<Int>): Result<EpisodeReponseDto> =
        httpClient.makeRequest {
            url("${BASE_URL}/character/${episodesIds.fastJoinToString(separator = ",", prefix = "[", postfix = "]")}")
            method = HttpMethod.Companion.Get
        }

}