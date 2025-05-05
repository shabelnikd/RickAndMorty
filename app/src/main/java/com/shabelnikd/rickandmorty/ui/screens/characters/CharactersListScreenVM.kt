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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersListScreenVM(
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


    private val favoriteCharacterIdsFlow: Flow<List<Int>> =
        favoriteCharactersRepository.getAllFavoriteCharactersIds()
            .filterNotNull()


    private val _favoritesListState =
        MutableStateFlow<UiState<List<CharacterWithFavoriteStatus>>>(UiState.NotLoaded)
    val favoritesListState: StateFlow<UiState<List<CharacterWithFavoriteStatus>>> =
        _favoritesListState.asStateFlow()


    private var lastProcessedFavoriteIds: List<Int> = emptyList()


    init {
        viewModelScope.launch {
            favoriteCharacterIdsFlow
                .filterNotNull()
                .combine(_favoritesListLoadRetryTrigger.onStart { emit(Unit) }) { favoriteIds, _ -> favoriteIds }
                .collect { currentFavoriteIds ->

                    val oldFavoriteIds = lastProcessedFavoriteIds
                    lastProcessedFavoriteIds = currentFavoriteIds

                    val addedIds = currentFavoriteIds.minus(oldFavoriteIds.toSet())
                    val removedIds = oldFavoriteIds.minus(currentFavoriteIds.toSet())

                    val currentCharacters =
                        (_favoritesListState.value as? UiState.Success)?.data ?: emptyList()

                    if (removedIds.isNotEmpty()) {
                        val updatedList =
                            currentCharacters.filter { it.characterModel.id !in removedIds }
                        _favoritesListState.value = UiState.Success(updatedList)
                    }

                    val isInitialLoadOrRetry = oldFavoriteIds.isEmpty() || addedIds.isNotEmpty()

                    if (isInitialLoadOrRetry) {
                        _favoritesListState.value = UiState.Loading

                        val result = favoriteCharactersRepository.getFullFavoriteCharacters(
                            currentFavoriteIds
                        )

                        result.fold(
                            onSuccess = { characters ->
                                val charactersWithStatus = characters.map { character ->
                                    CharacterWithFavoriteStatus(
                                        characterModel = character,
                                        isFavorite = true
                                    )
                                }
                                _favoritesListState.value = UiState.Success(charactersWithStatus)
                            },
                            onFailure = { throwable ->
                                val errorMessage =
                                    throwable.localizedMessage ?: "Ошибка при загрузке избранных"
                                _favoritesListState.value = UiState.Error(errorMessage)
                            }
                        )
                    }
                }
        }
    }


    private val _totalCharacterCount = MutableStateFlow<Int>(-1)
    val totalCharacterCount: StateFlow<Int> =
        _totalCharacterCount.asStateFlow()

    init {
        viewModelScope.launch {
            val result = charactersRepository.getCharacterTotalInfo()
            result.fold(
                onSuccess = { info ->
                    _totalCharacterCount.value = info.count
                },
                onFailure = { throwable ->
                    _totalCharacterCount.value = -1
                }
            )
        }
    }


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
    }

    fun updateStatusFilter(status: String?) {
        _statusFilter.value = status
    }

    fun updateGenderFilter(gender: String?) {
        _genderFilter.value = gender
    }

    fun updateSpeciesFilter(species: String?) {
        _speciesFilter.value = species
    }

    fun updateTypeFilter(type: String?) {
        _typeFilter.value = type
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