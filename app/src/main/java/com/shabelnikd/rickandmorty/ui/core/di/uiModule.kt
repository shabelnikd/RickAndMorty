package com.shabelnikd.rickandmorty.ui.core.di

import coil3.ImageLoader
import com.shabelnikd.rickandmorty.data.repository.characters.CharactersRepository
import com.shabelnikd.rickandmorty.data.repository.edisodes.EpisodesRepository
import com.shabelnikd.rickandmorty.data.repository.locations.LocationsRepository
import com.shabelnikd.rickandmorty.ui.screens.characters.CharactersListScreenVM
import com.shabelnikd.rickandmorty.ui.screens.characters.detail.CharacterDetailScreenVM
import com.shabelnikd.rickandmorty.ui.screens.episodes.EpisodesListScreenVM
import com.shabelnikd.rickandmorty.ui.screens.episodes.detail.EpisodeDetailScreenVM
import com.shabelnikd.rickandmorty.ui.screens.locations.LocationsListScreenVM
import com.shabelnikd.rickandmorty.ui.screens.locations.detail.LocationDetailScreenVM
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel {
        CharactersListScreenVM(
            charactersRepository = get(),
            favoriteCharactersRepository = get()
        )
    }
    viewModel {
        CharacterDetailScreenVM(
            charactersRepository = get(),
            imageLoader = get(),
            context = androidContext(),
            favoriteCharactersRepository = get()
        )
    }

    viewModel {
        EpisodesListScreenVM(
            episodesRepository = get()
        )
    }
    viewModel {
        EpisodeDetailScreenVM(
            episodesRepository = get()
        )
    }
    viewModel {
        LocationsListScreenVM(
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