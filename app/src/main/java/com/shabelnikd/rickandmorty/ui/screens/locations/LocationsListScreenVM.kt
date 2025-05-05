package com.shabelnikd.rickandmorty.ui.screens.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shabelnikd.rickandmorty.data.repository.locations.LocationsRepository
import com.shabelnikd.rickandmorty.domain.models.locations.Location
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

class LocationsListScreenVM(
    locationsRepository: LocationsRepository
) : ViewModel(), KoinComponent {

    val locationsPagingFlow: Flow<PagingData<Location>> =
        locationsRepository.getLocations()
            .cachedIn(viewModelScope)

}