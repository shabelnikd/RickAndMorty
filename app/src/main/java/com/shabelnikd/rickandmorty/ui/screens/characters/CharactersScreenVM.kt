package com.shabelnikd.rickandmorty.ui.screens.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shabelnikd.rickandmorty.data.repository.characters.CharactersRepository
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

class CharactersScreenVM(
    charactersRepository: CharactersRepository
) : ViewModel(), KoinComponent {

    val charactersPagingFlow: Flow<PagingData<Character>> =
        charactersRepository.getCharacters()
            .cachedIn(viewModelScope)

}