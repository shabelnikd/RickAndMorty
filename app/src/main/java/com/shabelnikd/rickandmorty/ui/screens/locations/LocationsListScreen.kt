package com.shabelnikd.rickandmorty.ui.screens.locations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.shabelnikd.rickandmorty.domain.models.locations.Location
import com.shabelnikd.rickandmorty.ui.components.CenteredTopBar
import com.shabelnikd.rickandmorty.ui.components.LocationListItem
import com.shabelnikd.rickandmorty.ui.components.RefreshableScaffoldPagingList
import com.shabelnikd.rickandmorty.ui.navigation.Screens
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocationsListScreen(navController: NavController, bottomBar: @Composable () -> Unit) {
    val vm = koinViewModel<LocationsScreenVM>()
    val locations = vm.locationsPagingFlow.collectAsLazyPagingItems()

    val listState = rememberLazyListState()

    val topBar = @Composable {
        CenteredTopBar(
            text = "Эпизоды",
            imageVector = Icons.Filled.Refresh
        ) {
            locations.refresh()
        }
    }

    val emptyListItem = @Composable {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Эпизоды не найдены")
        }
    }

    RefreshableScaffoldPagingList<Location>(
        modifier = Modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        emptyListItem = emptyListItem,
        items = locations,
        listState = listState,
        itemsContent = { item ->
            LocationListItem(item) {
                with(Screens.LocationDetailScreen) {
                    navController.navigate(route = "$route/${item.id}")
                }
            }


        },
    )
}