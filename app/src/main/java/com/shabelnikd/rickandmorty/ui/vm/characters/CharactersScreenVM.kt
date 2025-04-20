package com.shabelnikd.rickandmorty.ui.vm.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shabelnikd.rickandmorty.data.repository.characters.CharactersRepository
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class CharactersScreenVM(
    private val charactersRepository: CharactersRepository
) : ViewModel(), KoinComponent {

    val charactersPagingFlow: Flow<PagingData<Character>> =
        charactersRepository.getCharacters()
            .cachedIn(viewModelScope)

}