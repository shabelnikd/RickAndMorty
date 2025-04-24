package com.shabelnikd.rickandmorty.ui.core.di

import coil3.ImageLoader
import com.shabelnikd.rickandmorty.data.repository.characters.CharactersRepository
import com.shabelnikd.rickandmorty.data.repository.edisodes.EpisodesRepository
import com.shabelnikd.rickandmorty.data.repository.locations.LocationsRepository
import com.shabelnikd.rickandmorty.ui.screens.characters.CharactersScreenVM
import com.shabelnikd.rickandmorty.ui.screens.characters.detail.CharacterDetailScreenVM
import com.shabelnikd.rickandmorty.ui.screens.episodes.EpisodesScreenVM
import com.shabelnikd.rickandmorty.ui.screens.episodes.detail.EpisodeDetailScreenVM
import com.shabelnikd.rickandmorty.ui.screens.locations.LocationsScreenVM
import com.shabelnikd.rickandmorty.ui.screens.locations.detail.LocationDetailScreenVM
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { CharactersScreenVM(charactersRepository = get()) }
    viewModel {
        CharacterDetailScreenVM(
            charactersRepository = get(),
            imageLoader = get(),
            context = androidContext(),
            favoriteCharactersRepository = get()
        )
    }

    viewModel {
        EpisodesScreenVM(
            episodesRepository = get()
        )
    }
    viewModel {
        EpisodeDetailScreenVM(
            episodesRepository = get()
        )
    }
    viewModel {
        LocationsScreenVM(
            locationsRepository = get()
        )
    }
    viewModel {
        LocationDetailScreenVM(
            locationsRepository = get()
        )
    }

    single {
        CharactersRepository(api = get())
    }
    single {
        EpisodesRepository(api = get())
    }
    single {
        LocationsRepository(api = get())
    }

    single {
        ImageLoader.Builder(context = androidContext()).build()
    }
}