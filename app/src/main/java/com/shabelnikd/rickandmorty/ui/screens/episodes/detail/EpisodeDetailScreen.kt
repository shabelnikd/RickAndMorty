package com.shabelnikd.rickandmorty.ui.screens.episodes.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.shabelnikd.rickandmorty.domain.models.episodes.Episode
import com.shabelnikd.rickandmorty.ui.core.base.components.BaseDetailScreen
import com.shabelnikd.rickandmorty.ui.core.base.components.StyledTextKeyValueList
import org.koin.androidx.compose.koinViewModel

@Composable
fun EpisodeDetailScreen(episodeId: Int, navController: NavController) {
    val vm = koinViewModel<EpisodeDetailScreenVM>()
    val episodeState by vm.episodesState.collectAsStateWithLifecycle()

    LaunchedEffect(episodeId) {
        if (episodeId != -1) {
            vm.getEpisodeById(episodeId)
        } else {
            navController.popBackStack()
        }
    }

    BaseDetailScreen<Episode>(
        itemKey = episodeId,
        itemState = episodeState,
        animate = false,
        progressDelayMillis = 500
    ) { item ->
        Column(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StyledTextKeyValueList(
                color = MaterialTheme.colorScheme.onBackground,
                filterValue = "unknown",
                "Название эпизода: " to item.name,
                "Эпизод: " to item.episode,
                "Дата выхода: " to item.airDate,
            )
        }
    }
}