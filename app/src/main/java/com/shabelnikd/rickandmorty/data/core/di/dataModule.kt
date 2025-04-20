package com.shabelnikd.rickandmorty.data.core.di

import com.shabelnikd.rickandmorty.data.datasource.network.api.characters.CharacterApiService
import org.koin.dsl.module

val dataModule = module {
    single { CharacterApiService(get()) }
}