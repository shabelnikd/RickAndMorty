package com.shabelnikd.rickandmorty.ui.screens.characters.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.shabelnikd.rickandmorty.R
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import com.shabelnikd.rickandmorty.ui.base.BaseViewModel
import com.shabelnikd.rickandmorty.ui.components.LoadingIndicator
import com.shabelnikd.rickandmorty.ui.components.StyledTextKeyValue
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterDetailScreen(characterId: Int?, navController: NavController, modifier: Modifier) {
    val vm = koinViewModel<CharacterDetailScreenVM>()

    LaunchedEffect(characterId) {
        if (characterId != null && characterId != -1) {
            vm.loadCharacterDetails(characterId = characterId)
        } else {
            navController.popBackStack()
        }
    }

    val characterState by vm.characterState.collectAsStateWithLifecycle()
    val isFavorite by vm.isFavorite.collectAsStateWithLifecycle()
    val dominantColor by vm.dominantColor.collectAsStateWithLifecycle()


    when (characterState) {
        is BaseViewModel.UiState.Success<Character> -> {
            val character = (characterState as BaseViewModel.UiState.Success<Character>).data


            Column(
                modifier = modifier
                    .background(
                        Color.Transparent, shape = MaterialTheme.shapes.small
                    )
                    .padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.CenterHorizontally)
                ) {

                    Box(contentAlignment = Alignment.BottomEnd) {
                        AsyncImage(
                            model = character.image,
                            contentDescription = "Character image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = Shapes().small),
                        )

                        val contentColor = Color(0xFF9C0303)

                        val unfilledIcon = @Composable {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_favorite_unfilled),
                                contentDescription = "Favorite"
                            )
                        }

                        val filledIcon = @Composable {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_favorite_filled),
                                contentDescription = "Favorite"
                            )
                        }

                        IconToggleButton(
                            checked = isFavorite == true,
                            onCheckedChange = {
                                vm.toggleFavoriteStatus(character.id, isFavorite == true)
                            },
                            colors = IconButtonDefaults.iconToggleButtonColors().copy(
                                checkedContentColor = contentColor,
                                contentColor = contentColor
                            )
                        ) {
                            takeUnless { isFavorite == true }?.let {
                                unfilledIcon()
                            } ?: run {
                                filledIcon()
                            }


                        }

                    }


                }

                Spacer(modifier = Modifier.size(4.dp))

                Column(
                    modifier = Modifier.align(alignment = Alignment.End),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val attributes = mapOf<String, String>(
                        "Имя: " to character.name,
                        "Пол: " to character.gender,
                        "Раса: " to character.species,
                        "Статус: " to character.status,
                        "Обитает: " to character.origin.name,
                        "Локация: " to character.characterLocation.name
                    )

                    for ((key, value) in attributes) {
                        takeUnless { value.lowercase() == "unknown" }?.let {
                            StyledTextKeyValue(
                                rowModifier = Modifier,
                                key = key,
                                value = value,
                                color = dominantColor ?: Color.White
                            )
                        }


                    }

                }
            }

        }

        is BaseViewModel.UiState.NotLoaded -> {
            Text("Еще не загружено")
        }

        is BaseViewModel.UiState.Loading -> {
            LoadingIndicator()
        }

        else -> {
            Text("Ошибка")
        }
    }
}



