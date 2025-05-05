package com.shabelnikd.rickandmorty.ui.core.base.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.shabelnikd.rickandmorty.ui.navigation.TopLevelRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    scrollBehavior: BottomAppBarScrollBehavior
) {
    val navBackStackEntryState = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntryState.value?.destination


    BottomAppBar(
        modifier = modifier,
        containerColor = Color.Transparent,
        scrollBehavior = scrollBehavior
    ) {
        TopLevelRoute.TopLevelRoutes.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.route == item.route,
                onClick = {
                    val navOptions = navOptions {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    navController.navigate(item.route, navOptions)
                },
                icon = {
                    ImageVector.runCatching {
                        Icon(
                            imageVector = vectorResource(item.iconResId),
                            contentDescription = item.name
                        )
                    }.onFailure {

                    }

                },
                label = {
                    Text(item.name)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.surfaceContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

