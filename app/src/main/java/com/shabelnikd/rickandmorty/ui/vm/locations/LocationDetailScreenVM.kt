package com.shabelnikd.rickandmorty.ui.vm.locations

import com.shabelnikd.rickandmorty.data.repository.characters.CharactersRepository
import com.shabelnikd.rickandmorty.data.repository.locations.LocationsRepository
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import com.shabelnikd.rickandmorty.domain.models.locations.Location
import com.shabelnikd.rickandmorty.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class LocationDetailScreenVM(
    private val locationsRepository: LocationsRepository
) : BaseViewModel(), KoinComponent {

    private val _locationState =
        MutableStateFlow<UiState<Location>>(UiState.NotLoaded)
    val locationState = _locationState.asStateFlow()

    fun getLocationById(locationId: Int) {
        collectFlow(
            request = { locationsRepository.getLocationById(locationId = locationId) },
            stateFlow = _locationState,
            onSuccess = {
                // TODO
            },
            onError = {
                // TODO
            }
        )

    }

}