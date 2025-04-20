package com.shabelnikd.rickandmorty.ui.vm.episodes

import com.shabelnikd.rickandmorty.data.repository.characters.CharactersRepository
import com.shabelnikd.rickandmorty.data.repository.edisodes.EpisodesRepository
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import com.shabelnikd.rickandmorty.domain.models.episodes.Episode
import com.shabelnikd.rickandmorty.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class EpisodeDetailScreenVM(
    private val episodesRepository: EpisodesRepository
) : BaseViewModel(), KoinComponent {

    private val _episodesState =
        MutableStateFlow<UiState<Episode>>(UiState.NotLoaded)
    val episodesState = _episodesState.asStateFlow()

    fun getEpisodeById(episodeId: Int) {
        collectFlow(
            request = { episodesRepository.getEpisodeById(episodeId = episodeId) },
            stateFlow = _episodesState,
            onSuccess = {
                // TODO
            },
            onError = {
                // TODO
            }
        )

    }

}