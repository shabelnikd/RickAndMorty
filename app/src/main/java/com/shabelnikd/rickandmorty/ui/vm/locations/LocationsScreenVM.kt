package com.shabelnikd.rickandmorty.ui.vm.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shabelnikd.rickandmorty.data.repository.characters.CharactersRepository
import com.shabelnikd.rickandmorty.data.repository.locations.LocationsRepository
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import com.shabelnikd.rickandmorty.domain.models.locations.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class LocationsScreenVM(
    locationsRepository: LocationsRepository
) : ViewModel(), KoinComponent {

    val locationsPagingFlow: Flow<PagingData<Location>> =
        locationsRepository.getLocations()
            .cachedIn(viewModelScope)

}