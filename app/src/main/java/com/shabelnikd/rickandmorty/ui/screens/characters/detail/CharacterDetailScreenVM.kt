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
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import com.shabelnikd.rickandmorty.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import kotlin.math.max
import kotlin.math.min

@SuppressLint("StaticFieldLeak")
class CharacterDetailScreenVM(
    private val charactersRepository: CharactersRepository,
    private val imageLoader: ImageLoader,
    private val context: Context
) : BaseViewModel(), KoinComponent {

    private val _characterState =
        MutableStateFlow<UiState<Character>>(UiState.NotLoaded)
    val characterState = _characterState.asStateFlow()

    private val _dominantColor = MutableStateFlow<Color?>(null)
    val dominantColor = _dominantColor.asStateFlow()

    fun getCharacterById(characterId: Int) {
        collectFlow<Character>(
            request = { charactersRepository.getCharacterById(characterId = characterId) },
            stateFlow = _characterState,
            onSuccess = { character ->
                if (character.image.isNotEmpty()) {
                    extractDominantColorFromImage(character.image)
                } else {
                    _dominantColor.value = null
                }

            },
            onError = {
                // TODO
            }
        )

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

                    Log.e("ALLSD", "LOL $currentLightness")

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

