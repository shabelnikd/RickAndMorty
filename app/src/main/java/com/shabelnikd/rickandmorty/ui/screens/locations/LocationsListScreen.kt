package com.shabelnikd.rickandmorty.ui.screens.locations

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.compose.collectAsLazyPagingItems
import com.shabelnikd.rickandmorty.domain.models.locations.Location
import com.shabelnikd.rickandmorty.ui.components.LocationListItem
import com.shabelnikd.rickandmorty.ui.components.SimpleShimmeringPlaceholder
import com.shabelnikd.rickandmorty.ui.core.base.components.RefreshableScaffoldPagingList
import com.shabelnikd.rickandmorty.ui.core.base.components.TopBar
import com.shabelnikd.rickandmorty.ui.navigation.Screens
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsListScreen(navController: NavController) {
    val vm = koinViewModel<LocationsListScreenVM>()
    val locations = vm.locationsPagingFlow.collectAsLazyPagingItems()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var blurEnabled by remember { mutableStateOf(false) }

    val scrollBehaviorTop = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(currentRoute) {
        if (currentRoute == Screens.LocationsListScreen.route) {
            blurEnabled = false
        }
    }

    val listState = rememberLazyListState()

    val topBar = @Composable {
        TopBar(
            text = "Локации",
            imageVector = Icons.Filled.Refresh,
            modifier = Modifier,
            scrollBehavior = scrollBehaviorTop
        ) {
            locations.refresh()
        }
    }


    RefreshableScaffoldPagingList<Location>(
        topBar = { topBar() },
        items = locations,
        listState = listState,
        shimmerItem = { isShimmer ->
            SimpleShimmeringPlaceholder(isShimmer)
        },
        blurEnabled = blurEnabled,
        navController = navController,
    ) { item ->

        LocationListItem(item) {
            with(Screens.LocationDetailScreen) {
                blurEnabled = true
                navController.navigate(route = "$route/${item.id}")
            }
        }
    }

}