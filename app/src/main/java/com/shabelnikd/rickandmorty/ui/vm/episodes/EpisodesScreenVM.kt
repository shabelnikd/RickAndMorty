package com.shabelnikd.rickandmorty.ui.vm.episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shabelnikd.rickandmorty.data.repository.characters.CharactersRepository
import com.shabelnikd.rickandmorty.data.repository.edisodes.EpisodesRepository
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import com.shabelnikd.rickandmorty.domain.models.episodes.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class EpisodesScreenVM(
    episodesRepository: EpisodesRepository
) : ViewModel(), KoinComponent {

    val episodesPagingFlow: Flow<PagingData<Episode>> =
        episodesRepository.getEpisodes()
            .cachedIn(viewModelScope)

}