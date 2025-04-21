package com.shabelnikd.rickandmorty.domain.models.locations


data class LocationResponse(
    val info: Info,
    val results: List<Location>
)