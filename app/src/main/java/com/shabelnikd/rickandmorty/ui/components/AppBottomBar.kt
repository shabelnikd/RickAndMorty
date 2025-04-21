package com.shabelnikd.rickandmorty.ui.components

import android.annotation.SuppressLint
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.shabelnikd.rickandmorty.ui.navigation.TopLevelRoute


@SuppressLint("RestrictedApi")
@Composable
fun AppBottomBar(
    navController: NavController,
    topLevelRoutes: List<TopLevelRoute<String>>
) {
    val navBackStackEntryState = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntryState.value?.destination

    NavigationBar {
        topLevelRoutes.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

            NavigationBarItem(
                selected = selected,
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
                    Icon(imageVector = item.icon, contentDescription = item.name)
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

