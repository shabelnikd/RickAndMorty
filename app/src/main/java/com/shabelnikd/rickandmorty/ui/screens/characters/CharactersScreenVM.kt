package com.shabelnikd.rickandmorty.ui.screens.characters

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.shabelnikd.rickandmorty.data.repository.characters.CharactersRepository
import com.shabelnikd.rickandmorty.data.repository.characters.FavoriteCharactersRepositoryImpl
import com.shabelnikd.rickandmorty.domain.models.characters.CharacterModel
import com.shabelnikd.rickandmorty.domain.models.characters.CharacterWithFavoriteStatus
import com.shabelnikd.rickandmorty.ui.base.BaseViewModel
import com.shabelnikd.rickandmorty.ui.models.AllFilters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersScreenVM(
    charactersRepository: CharactersRepository,
    private val favoriteCharactersRepository: FavoriteCharactersRepositoryImpl
) : BaseViewModel(), KoinComponent {

    private val _favoritesSheetVisible = MutableStateFlow(false)
    val showFavoritesSheet: StateFlow<Boolean> = _favoritesSheetVisible.asStateFlow()

    private val _favoritesListLoadRetryTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    fun openFavoritesSheet() {
        _favoritesSheetVisible.value = true
    }

    fun closeFavoritesSheet() {
        _favoritesSheetVisible.value = false
    }

    fun retryFavoritesLoad() {
        viewModelScope.launch {
            _favoritesListLoadRetryTrigger.emit(Unit)
        }
    }


    private val _filterSheetVisible = MutableStateFlow(false)
    val showFilterSheet: StateFlow<Boolean> = _filterSheetVisible.asStateFlow()

    fun openFilterSheet() {
        _filterSheetVisible.value = true
    }

    fun closeFilterSheet() {
        _filterSheetVisible.value = false
    }


    val favoritesListState =
        _favoritesSheetVisible
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { isVisible ->
                if (isVisible) {
                    combine(
                        favoriteCharactersRepository
                            .getAllFavoriteCharactersIds()
                            .filterNotNull()
                            .distinctUntilChanged(),
                        _favoritesListLoadRetryTrigger.onStart { emit(Unit) }
                    ) { favoriteIds, _ -> favoriteIds }.flatMapLatest { favoriteIds ->
                        flow {
                            emit(UiState.Loading)
                            val result =
                                favoriteCharactersRepository.getFullFavoriteCharacters(favoriteIds)

                            result.fold(
                                onSuccess = { characters ->
                                    val charactersWithStatus = characters.map { character ->
                                        CharacterWithFavoriteStatus(
                                            characterModel = character,
                                            isFavorite = true
                                        )
                                    }
                                    emit(UiState.Success(charactersWithStatus))
                                },
                                onFailure = { throwable ->
                                    val errorMessage = throwable.localizedMessage ?: "Ошибка"
                                    emit(UiState.Error(errorMessage))
                                }
                            )
                        }
                    }
                } else {
                    flowOf(UiState.NotLoaded as UiState<List<CharacterWithFavoriteStatus>>)
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.NotLoaded
            )


    private val favoriteCharacterIdsFlow: Flow<List<Int>> =
        favoriteCharactersRepository.getAllFavoriteCharactersIds()
            .filterNotNull()
            .distinctUntilChanged()


    private val _showImmediateLoading = MutableStateFlow(false)
    val showImmediateLoading: StateFlow<Boolean> = _showImmediateLoading.asStateFlow()


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _statusFilter = MutableStateFlow<String?>(null)
    val statusFilter: StateFlow<String?> = _statusFilter.asStateFlow()

    private val _genderFilter = MutableStateFlow<String?>(null)
    val genderFilter: StateFlow<String?> = _genderFilter.asStateFlow()

    private val _speciesFilter = MutableStateFlow<String?>(null)
    val speciesFilter: StateFlow<String?> = _speciesFilter.asStateFlow()

    private val _typeFilter = MutableStateFlow<String?>(null)
    val typeFilter: StateFlow<String?> = _typeFilter.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        _showImmediateLoading.value = true
    }

    fun updateStatusFilter(status: String?) {
        _statusFilter.value = status
        _showImmediateLoading.value = true
    }

    fun updateGenderFilter(gender: String?) {
        _genderFilter.value = gender
        _showImmediateLoading.value = true
    }

    fun updateSpeciesFilter(species: String?) {
        _speciesFilter.value = species
        _showImmediateLoading.value = true
    }

    fun updateTypeFilter(type: String?) {
        _typeFilter.value = type
        _showImmediateLoading.value = true
    }


    private val allFiltersFlow: Flow<AllFilters> =
        combine(
            searchQuery,
            statusFilter,
            genderFilter,
            speciesFilter,
            typeFilter
        ) { query, status, gender, species, type ->
            AllFilters(query, status, gender, species, type)
        }


    @OptIn(FlowPreview::class)
    private val charactersPagingFlowWithFilters: Flow<PagingData<CharacterModel>> =
        allFiltersFlow
            .debounce(700L)
            .flatMapLatest { filters ->
                _showImmediateLoading.value = false
                charactersRepository.getCharacters(
                    query = filters.query,
                    status = filters.status,
                    gender = filters.gender,
                    species = filters.species,
                    type = filters.type
                )
            }
            .cachedIn(viewModelScope)

    val charactersPagingFlowWithFavoriteStatus: Flow<PagingData<CharacterWithFavoriteStatus>> =
        charactersPagingFlowWithFilters
            .combine(favoriteCharacterIdsFlow) { pagingData, favoriteIds ->
                pagingData.map { characterModel ->
                    CharacterWithFavoriteStatus(
                        characterModel = characterModel,
                        isFavorite = favoriteIds.contains(characterModel.id)
                    )
                }
            }
            .cachedIn(viewModelScope)


    fun toggleFavoriteStatus(characterId: Int, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch {
            if (isCurrentlyFavorite) {
                favoriteCharactersRepository.removeFavoriteCharacter(characterId)
            } else {
                favoriteCharactersRepository.addFavoriteCharacter(characterId)
            }
        }
    }

}