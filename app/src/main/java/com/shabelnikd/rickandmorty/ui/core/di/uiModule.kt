package com.shabelnikd.rickandmorty.ui.core.di

import com.shabelnikd.rickandmorty.data.repository.characters.CharactersRepository
import com.shabelnikd.rickandmorty.ui.vm.characters.CharactersScreenVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { CharactersScreenVM(get()) }
    single { CharactersRepository(get()) }
}