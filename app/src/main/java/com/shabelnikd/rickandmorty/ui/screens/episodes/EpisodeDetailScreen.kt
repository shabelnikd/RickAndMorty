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
import com.shabelnikd.rickandmorty.domain.models.episodes.Episode
import com.shabelnikd.rickandmorty.ui.base.BaseViewModel
import com.shabelnikd.rickandmorty.ui.vm.characters.CharacterDetailScreenVM
import com.shabelnikd.rickandmorty.ui.vm.episodes.EpisodeDetailScreenVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun EpisodeDetailScreen(episodeId: Int) {
    val vm = koinViewModel<EpisodeDetailScreenVM>()
    val scope = rememberCoroutineScope()
    val episodeState by vm.episodesState.collectAsStateWithLifecycle()

    LaunchedEffect(scope) {
        vm.getEpisodeById(episodeId = episodeId)
    }

    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (episodeState) {
                is BaseViewModel.UiState.Success<Episode> -> {
                    val episode =
                        (episodeState as BaseViewModel.UiState.Success<Episode>).data

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val textStyle = MaterialTheme.typography.bodyLarge

                            Row {
                                Text(
                                    text = "Название эпизода: ",
                                    fontStyle = FontStyle.Italic,
                                    style = textStyle
                                )
                                Text(text = episode.name, style = textStyle)
                            }

                            Row {
                                Text(
                                    text = "Эпизод: ",
                                    fontStyle = FontStyle.Italic,
                                    style = textStyle
                                )
                                Text(text = episode.episode, style = textStyle)
                            }

                            Row {
                                Text(
                                    text = "Дата выхода: ",
                                    fontStyle = FontStyle.Italic,
                                    style = textStyle
                                )
                                Text(text = episode.airDate, style = textStyle)
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