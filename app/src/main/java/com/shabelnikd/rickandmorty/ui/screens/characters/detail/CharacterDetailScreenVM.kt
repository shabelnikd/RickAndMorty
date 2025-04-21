package com.shabelnikd.rickandmorty.ui.screens.characters.detail

import com.shabelnikd.rickandmorty.data.repository.characters.CharactersRepository
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import com.shabelnikd.rickandmorty.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class CharacterDetailScreenVM(
    private val charactersRepository: CharactersRepository
) : BaseViewModel(), KoinComponent {

    private val _characterState =
        MutableStateFlow<UiState<Character>>(UiState.NotLoaded)
    val characterState = _characterState.asStateFlow()

    fun getCharacterById(characterId: Int) {
        collectFlow(
            request = { charactersRepository.getCharacterById(characterId = characterId) },
            stateFlow = _characterState,
            onSuccess = {
                // TODO
            },
            onError = {
                // TODO
            }
        )

    }

}