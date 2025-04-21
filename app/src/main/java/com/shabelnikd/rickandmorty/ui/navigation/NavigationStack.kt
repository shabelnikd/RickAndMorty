package com.shabelnikd.rickandmorty.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shabelnikd.rickandmorty.ui.components.AppBottomBar
import com.shabelnikd.rickandmorty.ui.screens.characters.CharacterDetailScreen
import com.shabelnikd.rickandmorty.ui.screens.characters.CharactersListScreen
import com.shabelnikd.rickandmorty.ui.screens.episodes.EpisodeDetailScreen
import com.shabelnikd.rickandmorty.ui.screens.episodes.EpisodesListScreen
import com.shabelnikd.rickandmorty.ui.screens.locations.LocationDetailScreen
import com.shabelnikd.rickandmorty.ui.screens.locations.LocationsListScreen


@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    val topLevelRoutes = listOf<TopLevelRoute<String>>(
        TopLevelRoute(
            name = "Персонажи",
            route = Screens.CharactersListScreen.route,
            icon = Icons.AutoMirrored.Default.List
        ),
        TopLevelRoute(
            name = "Эпизоды",
            route = Screens.EpisodesListScreen.route,
            icon = Icons.AutoMirrored.Default.List
        ),
        TopLevelRoute(
            name = "Локации",
            route = Screens.LocationsListScreen.route,
            icon = Icons.AutoMirrored.Default.List
        )
    )

    val bottomAppBar = @Composable {
        AppBottomBar(
            navController = navController,
            topLevelRoutes = topLevelRoutes
        )
    }

    NavHost(
        navController = navController, startDestination = Screens.EpisodesListScreen.route
    ) {
        composable(
            route = Screens.CharactersListScreen.route,
            content = {
                CharactersListScreen(
                    navController = navController,
                    bottomAppBar = bottomAppBar
                )
            })

        composable(
            route = Screens.EpisodesListScreen.route,
            content = {
                EpisodesListScreen(
                    navController = navController,
                    bottomAppBar = bottomAppBar
                )
            })

        composable(
            route = Screens.LocationsListScreen.route,
            content = {
                LocationsListScreen(
                    navController = navController,
                    bottomAppBar = bottomAppBar
                )
            })


        with(Screens.EpisodeDetailScreen) {
            composable(
                route = "$route?$argName={$argName}", arguments = listOf(
                    navArgument("$argName") {
                        type = NavType.IntType
                        nullable = false
                    })
            ) { backStackEntry ->
                EpisodeDetailScreen(
                    episodeId = backStackEntry.arguments?.getInt("$argName") ?: 0
                )
            }
        }

        with(Screens.LocationDetailScreen) {
            composable(
                route = "$route?$argName={$argName}", arguments = listOf(
                    navArgument("$argName") {
                        type = NavType.IntType
                        nullable = false
                    })
            ) { backStackEntry ->
                LocationDetailScreen(
                    locationId = backStackEntry.arguments?.getInt("$argName") ?: 0
                )
            }
        }

        with(Screens.CharacterDetailScreen) {
            composable(
                route = "$route?$argName={$argName}", arguments = listOf(
                    navArgument("$argName") {
                        type = NavType.IntType
                        nullable = false
                    })
            ) { backStackEntry ->
                CharacterDetailScreen(
                    characterId = backStackEntry.arguments?.getInt("$argName") ?: 0
                )
            }
        }


    }

}
