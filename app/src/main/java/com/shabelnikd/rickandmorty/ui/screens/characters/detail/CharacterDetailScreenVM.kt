package com.shabelnikd.rickandmorty.ui.screens.characters.detail

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import coil3.Bitmap
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.shabelnikd.rickandmorty.data.repository.characters.CharactersRepository
import com.shabelnikd.rickandmorty.data.repository.characters.FavoriteCharactersRepositoryImpl
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import com.shabelnikd.rickandmorty.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import kotlin.math.max
import kotlin.math.min

@SuppressLint("StaticFieldLeak")
@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailScreenVM(
    private val charactersRepository: CharactersRepository,
    private val imageLoader: ImageLoader,
    private val context: Context,
    private val favoriteCharactersRepository: FavoriteCharactersRepositoryImpl
) : BaseViewModel(), KoinComponent {

    private val _characterIdToLoad = MutableStateFlow<Int?>(null)

    fun loadCharacterDetails(characterId: Int) {
        _characterIdToLoad.value = characterId
    }


    val characterState: StateFlow<UiState<Character>> = _characterIdToLoad
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { id ->
            favoriteCharactersRepository.isCharacterFavorite(id)
                .distinctUntilChanged()
                .onEach { isFav -> _isFavorite.value = isFav }
                .launchIn(viewModelScope)

            flow {
                emit(UiState.Loading)
                charactersRepository.getCharacterById(id)
                    .onSuccess { character ->
                        emit(UiState.Success(character))
                        if (character.image.isNotEmpty()) {
                            extractDominantColorFromImage(character.image)
                        } else {
                            _dominantColor.value = null
                        }

                    }.onFailure { throwable ->
                        val errorMessage = throwable.localizedMessage ?: "Ошибка загрузки"
                        emit(UiState.Error(errorMessage))
                        _dominantColor.value = null
                    }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            UiState.NotLoaded
        )

    private val _isFavorite = MutableStateFlow<Boolean?>(null)
    val isFavorite = _isFavorite.asStateFlow()

    private val _dominantColor = MutableStateFlow<Color?>(null)
    val dominantColor = _dominantColor.asStateFlow()

    fun toggleFavoriteStatus(characterId: Int, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch {
            if (isCurrentlyFavorite) {
                favoriteCharactersRepository.removeFavoriteCharacter(characterId)
            } else {
                favoriteCharactersRepository.addFavoriteCharacter(characterId)
            }
        }
    }


    private fun extractDominantColorFromImage(imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val request =
                ImageRequest.Builder(context = context)
                    .data(imageUrl)
                    .allowHardware(false)
                    .build()

            val result = imageLoader.execute(request)

            if (result is SuccessResult) {
                val sourceBitmap = result.image.toBitmap()

                val targetWidth = 270
                val targetHeight = 270

                val sourceWidth = sourceBitmap.width
                val sourceHeight = sourceBitmap.height

                val cropWidth = min(sourceWidth, targetWidth)
                val cropHeight = min(sourceHeight, targetHeight)

                val cropX = max(0, (sourceWidth - cropWidth) / 2)
                val cropY = max(0, (sourceHeight - cropHeight) / 2)


                val croppedBitmap = Bitmap.createBitmap(
                    sourceBitmap,
                    cropX,
                    cropY,
                    cropWidth,
                    cropHeight
                )

                Palette.from(croppedBitmap).generate { palette ->
                    val color = palette?.vibrantSwatch?.hsl
                        ?: palette?.dominantSwatch?.hsl
                        ?: palette?.mutedSwatch?.hsl

                    val hue = color?.get(0) ?: 0f
                    val saturation = color?.get(1) ?: 1f
                    val currentLightness = color?.get(2) ?: 1f

                    val darkThreshold = 0.3f
                    val targetLightnessValue = 0.5f


                    val adjustLightness = if (currentLightness > darkThreshold) {
                        currentLightness
                    } else {
                        targetLightnessValue
                    }

                    val changedColor =
                        Color.hsl(hue = hue, saturation = saturation, lightness = adjustLightness)

                    _dominantColor.value = changedColor

                }
            } else {
                _dominantColor.value = null
            }
        }
    }
}

