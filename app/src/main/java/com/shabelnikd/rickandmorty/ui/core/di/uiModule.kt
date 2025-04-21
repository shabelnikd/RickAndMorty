package com.shabelnikd.rickandmorty.ui.core.di

import com.shabelnikd.rickandmorty.data.repository.characters.CharactersRepository
import com.shabelnikd.rickandmorty.data.repository.edisodes.EpisodesRepository
import com.shabelnikd.rickandmorty.data.repository.locations.LocationsRepository
import com.shabelnikd.rickandmorty.ui.screens.characters.CharactersScreenVM
import com.shabelnikd.rickandmorty.ui.screens.characters.detail.CharacterDetailScreenVM
import com.shabelnikd.rickandmorty.ui.screens.episodes.EpisodesScreenVM
import com.shabelnikd.rickandmorty.ui.screens.episodes.detail.EpisodeDetailScreenVM
import com.shabelnikd.rickandmorty.ui.screens.locations.LocationsScreenVM
import com.shabelnikd.rickandmorty.ui.screens.locations.detail.LocationDetailScreenVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { CharactersScreenVM(get()) }
    viewModel { CharacterDetailScreenVM(get()) }
    viewModel { EpisodesScreenVM(get()) }
    viewModel { EpisodeDetailScreenVM(get()) }
    viewModel { LocationsScreenVM(get()) }
    viewModel { LocationDetailScreenVM(get()) }

    single { CharactersRepository(get()) }
    single { EpisodesRepository(get()) }
    single { LocationsRepository(get()) }
}