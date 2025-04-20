package com.shabelnikd.rickandmorty.data.datasource.network.api.locations


import androidx.compose.ui.util.fastJoinToString
import com.shabelnikd.rickandmorty.data.core.utils.makeRequest
import com.shabelnikd.rickandmorty.data.datasource.network.api.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import com.shabelnikd.rickandmorty.data.models.episodes.EpisodeDto
import com.shabelnikd.rickandmorty.data.models.episodes.EpisodeReponseDto
import com.shabelnikd.rickandmorty.data.models.locations.LocationDto
import com.shabelnikd.rickandmorty.data.models.locations.LocationResponseDto


class LocationApiService(
    private val httpClient: HttpClient,
) {

    suspend fun getLocations(page: Int): Result<LocationResponseDto> =
        httpClient.makeRequest {
            url("${BASE_URL}/location")
            method = HttpMethod.Companion.Get
            parameter("page", page)
        }

    suspend fun getLocationById(locationId: Int): Result<LocationDto> =
        httpClient.makeRequest {
            url("${BASE_URL}/location/${locationId}")
            method = HttpMethod.Companion.Get
        }

    suspend fun getLocationsByIds(locationsIds: List<Int>): Result<LocationResponseDto> =
        httpClient.makeRequest {
            url("${BASE_URL}/location/${locationsIds.fastJoinToString(separator = ",", prefix = "[", postfix = "]")}")
            method = HttpMethod.Companion.Get
        }

}