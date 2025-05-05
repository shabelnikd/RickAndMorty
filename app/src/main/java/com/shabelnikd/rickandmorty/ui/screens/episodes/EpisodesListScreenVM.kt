package com.shabelnikd.rickandmorty.ui.screens.episodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shabelnikd.rickandmorty.data.repository.edisodes.EpisodesRepository
import com.shabelnikd.rickandmorty.domain.models.episodes.Episode
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

class EpisodesListScreenVM(
    episodesRepository: EpisodesRepository
) : ViewModel(), KoinComponent {

    val episodesPagingFlow: Flow<PagingData<Episode>> =
        episodesRepository.getEpisodes()
            .cachedIn(viewModelScope)

}