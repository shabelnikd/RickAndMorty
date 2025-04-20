package com.shabelnikd.rickandmorty.ui.screens.episodes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import com.shabelnikd.rickandmorty.ui.base.BaseViewModel
import com.shabelnikd.rickandmorty.ui.vm.characters.CharacterDetailScreenVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun EpisodeDetailScreen(characterId: Int) {
    val vm = koinViewModel<CharacterDetailScreenVM>()
    val scope = rememberCoroutineScope()
    val characterState by vm.characterState.collectAsStateWithLifecycle()

    LaunchedEffect(scope) {
        vm.getCharacterById(characterId = characterId)
    }

    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (characterState) {
                is BaseViewModel.UiState.Success<Character> -> {
                    val character =
                        (characterState as BaseViewModel.UiState.Success<Character>).data

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        val brush = remember {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.LightGray,
                                    Color.White
                                )
                            )
                        }

                        AsyncImage(
                            model = character.image,
                            contentDescription = "Character image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(size = 300.dp)
                                .clip(
                                    shape = Shapes().small
                                )
                                .align(alignment = Alignment.CenterHorizontally)
                                .border(
                                    border = BorderStroke(
                                        width = 2.dp,
                                        brush = brush
                                    ),
                                    shape = Shapes().small
                                )
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val textStyle = MaterialTheme.typography.bodyLarge

                            Row {
                                Text(
                                    text = "Имя персонажа: ",
                                    fontStyle = FontStyle.Italic,
                                    style = textStyle
                                )
                                Text(text = character.name, style = textStyle)
                            }

                            Row {
                                Text(
                                    text = "Статус: ",
                                    fontStyle = FontStyle.Italic,
                                    style = textStyle
                                )
                                Text(text = character.status, style = textStyle)
                            }

                            Row {
                                Text(
                                    text = "Пол: ",
                                    fontStyle = FontStyle.Italic,
                                    style = textStyle
                                )
                                Text(text = character.gender, style = textStyle)
                            }


                            Row {
                                Text(
                                    text = "Раса: ",
                                    fontStyle = FontStyle.Italic,
                                    style = textStyle
                                )
                                Text(text = character.species, style = textStyle)
                            }

                            Row {
                                Text(
                                    text = "Обитает: ",
                                    fontStyle = FontStyle.Italic,
                                    style = textStyle
                                )
                                Text(text = character.origin.name, style = textStyle)
                            }

                            Row {
                                Text(
                                    text = "Локация: ",
                                    fontStyle = FontStyle.Italic,
                                    style = textStyle
                                )
                                Text(text = character.characterLocation.name, style = textStyle)
                            }

                        }
                    }

                }

                is BaseViewModel.UiState.NotLoaded -> {
                    Text("Еще не загружено")
                }

                else -> {
                    Text("Ошибка")
                }
            }
        }

    }


}