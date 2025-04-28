package com.shabelnikd.rickandmorty.ui.screens.episodes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.compose.collectAsLazyPagingItems
import com.shabelnikd.rickandmorty.domain.models.episodes.Episode
import com.shabelnikd.rickandmorty.ui.components.EpisodeListItem
import com.shabelnikd.rickandmorty.ui.core.base.components.CenteredTopBar
import com.shabelnikd.rickandmorty.ui.core.base.components.ErrorMessage
import com.shabelnikd.rickandmorty.ui.core.base.components.RefreshableScaffoldPagingList
import com.shabelnikd.rickandmorty.ui.navigation.Screens
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodesListScreen(navController: NavController) {
    val vm = koinViewModel<EpisodesScreenVM>()
    val episodes = vm.episodesPagingFlow.collectAsLazyPagingItems()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var blur by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val scrollBehaviorTop = TopAppBarDefaults.enterAlwaysScrollBehavior()


    LaunchedEffect(currentRoute) {
        if (currentRoute == Screens.EpisodesListScreen.route) {
            blur = false
        }
    }

    val listState = rememberLazyListState()

    val topBar = @Composable {
        CenteredTopBar(
            text = "Эпизоды",
            imageVector = Icons.Filled.Refresh,
            modifier = Modifier,
            scrollBehavior = scrollBehaviorTop
        ) {
            episodes.refresh()
        }
    }


    RefreshableScaffoldPagingList<Episode>(
        blur = blur,
        topBar = topBar,
        navController = navController,
        items = episodes,
        listState = listState,
        scrollBehaviorTop = scrollBehaviorTop,
        emptyListItem = {
            ErrorMessage("Персонажи не найдены")
        },
        fab = {
            FloatingActionButton(onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            }, shape = CircleShape) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = "Arrow Up"
                )
            }
        }) { item ->

        EpisodeListItem(item) {
            with(Screens.EpisodeDetailScreen) {
                blur = true
                navController.navigate(route = "$route/${item.id}")
            }
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.6.dp)
    }

}


