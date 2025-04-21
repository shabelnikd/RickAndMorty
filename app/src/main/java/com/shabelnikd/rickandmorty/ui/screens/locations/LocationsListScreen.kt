package com.shabelnikd.rickandmorty.ui.screens.locations

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
import com.shabelnikd.rickandmorty.ui.components.LocationListItem
import com.shabelnikd.rickandmorty.ui.navigation.Screens
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocationsListScreen(navController: NavController, bottomAppBar: @Composable () -> Unit) {
    val vm = koinViewModel<LocationsScreenVM>()
    val locations = vm.locationsPagingFlow.collectAsLazyPagingItems()


    Scaffold(
        topBar = {
            CenteredTopBar(
                text = "Локации",
            ) {
                navController.navigateUp()
            }
        },
        bottomBar = bottomAppBar,

        ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn {
                items(locations.itemCount) { index ->
                    val location = locations[index]
                    location?.let {
                        LocationListItem(it) {
                            with(Screens.LocationDetailScreen) {
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