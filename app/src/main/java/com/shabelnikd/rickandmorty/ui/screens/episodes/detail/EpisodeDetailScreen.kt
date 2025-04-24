package com.shabelnikd.rickandmorty.ui.screens.episodes.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.shabelnikd.rickandmorty.domain.models.episodes.Episode
import com.shabelnikd.rickandmorty.ui.base.BaseViewModel
import com.shabelnikd.rickandmorty.ui.components.CenteredTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun EpisodeDetailScreen(episodeId: Int?, navController: NavController) {
    val vm = koinViewModel<EpisodeDetailScreenVM>()
    val scope = rememberCoroutineScope()
    val episodeState by vm.episodesState.collectAsStateWithLifecycle()
    val episodeNameState = remember { mutableStateOf("") }


    LaunchedEffect(scope) {
        episodeId?.let {
            vm.getEpisodeById(episodeId = episodeId)

        }
    }

    Scaffold(
        topBar = {
            CenteredTopBar(
                text = episodeNameState.value,
                imageVector = Icons.AutoMirrored.Filled.ArrowBack
            ) {
                navController.popBackStack()
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (episodeState) {
                is BaseViewModel.UiState.Success<Episode> -> {
                    val episode =
                        (episodeState as BaseViewModel.UiState.Success<Episode>).data

                    episodeNameState.value = episode.name

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