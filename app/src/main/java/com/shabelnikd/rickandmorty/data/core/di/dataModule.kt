package com.shabelnikd.rickandmorty.data.core.di

import androidx.room.Room
import com.shabelnikd.rickandmorty.data.core.room.AppDatabase
import com.shabelnikd.rickandmorty.data.datasource.network.api.characters.CharacterApiService
import com.shabelnikd.rickandmorty.data.datasource.network.api.episodes.EpisodeApiService
import com.shabelnikd.rickandmorty.data.datasource.network.api.locations.LocationApiService
import com.shabelnikd.rickandmorty.data.repository.characters.FavoriteCharactersRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { CharacterApiService(get()) }
    single { EpisodeApiService(get()) }
    single { LocationApiService(get()) }


    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().favoriteCharacterDao() }

    single {
        FavoriteCharactersRepositoryImpl(
            favoriteCharacterDao = get(),
            apiService = get()
        )
    }
}