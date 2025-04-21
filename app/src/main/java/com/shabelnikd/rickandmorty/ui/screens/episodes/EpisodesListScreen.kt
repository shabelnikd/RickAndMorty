package com.shabelnikd.rickandmorty.ui.screens.episodes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.shabelnikd.rickandmorty.ui.components.CenteredTopBar
import com.shabelnikd.rickandmorty.ui.components.EpisodeListItem
import com.shabelnikd.rickandmorty.ui.navigation.Screens
import org.koin.androidx.compose.koinViewModel

@Composable
fun EpisodesListScreen(navController: NavController, bottomAppBar: @Composable () -> Unit) {
    val vm = koinViewModel<EpisodesScreenVM>()
    val episodes = vm.episodesPagingFlow.collectAsLazyPagingItems()


    Scaffold(
        topBar = {
            CenteredTopBar(
                text = "Эпизоды",
            ) {
                navController.navigateUp()
            }
        },
        bottomBar = bottomAppBar,
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn {
                items(episodes.itemCount) { index ->
                    val episode = episodes[index]
                    episode?.let {
                        EpisodeListItem(it) {
                            with(Screens.EpisodeDetailScreen) {
                                navController.navigate(route = route + "?$argName=${it.id}")
                            }
                        }
                    }
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                }
            }
        }

    }


}