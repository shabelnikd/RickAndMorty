package com.shabelnikd.rickandmorty.data.core.di

import com.shabelnikd.rickandmorty.data.datasource.network.api.characters.CharacterApiService
import com.shabelnikd.rickandmorty.data.datasource.network.api.episodes.EpisodeApiService
import com.shabelnikd.rickandmorty.data.datasource.network.api.locations.LocationApiService
import org.koin.dsl.module

val dataModule = module {
    single { CharacterApiService(get()) }
    single { EpisodeApiService(get()) }
    single { LocationApiService(get()) }
}