package com.shabelnikd.rickandmorty.ui.core.di

import com.shabelnikd.rickandmorty.data.repository.characters.CharactersRepository
import com.shabelnikd.rickandmorty.data.repository.edisodes.EpisodesRepository
import com.shabelnikd.rickandmorty.data.repository.locations.LocationsRepository
import com.shabelnikd.rickandmorty.ui.vm.characters.CharacterDetailScreenVM
import com.shabelnikd.rickandmorty.ui.vm.characters.CharactersScreenVM
import com.shabelnikd.rickandmorty.ui.vm.episodes.EpisodeDetailScreenVM
import com.shabelnikd.rickandmorty.ui.vm.episodes.EpisodesScreenVM
import com.shabelnikd.rickandmorty.ui.vm.locations.LocationDetailScreenVM
import com.shabelnikd.rickandmorty.ui.vm.locations.LocationsScreenVM
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