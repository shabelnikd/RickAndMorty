package com.shabelnikd.rickandmorty.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shabelnikd.rickandmorty.R
import com.shabelnikd.rickandmorty.ui.components.AppBottomBar
import com.shabelnikd.rickandmorty.ui.components.CenteredTopBar
import com.shabelnikd.rickandmorty.ui.screens.characters.CharactersListScreen
import com.shabelnikd.rickandmorty.ui.screens.characters.detail.CharacterDetailScreen
import com.shabelnikd.rickandmorty.ui.screens.episodes.EpisodesListScreen
import com.shabelnikd.rickandmorty.ui.screens.episodes.detail.EpisodeDetailScreen
import com.shabelnikd.rickandmorty.ui.screens.locations.LocationsListScreen
import com.shabelnikd.rickandmorty.ui.screens.locations.detail.LocationDetailScreen


@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    val topLevelRoutes = listOf<TopLevelRoute<String>>(
        TopLevelRoute(
            name = "Персонажи",
            route = Screens.CharactersListScreen.route,
            icon = ImageVector.vectorResource(R.drawable.ic_characters)
        ),
        TopLevelRoute(
            name = "Эпизоды",
            route = Screens.EpisodesListScreen.route,
            icon = ImageVector.vectorResource(R.drawable.ic_episodes)
        ),
        TopLevelRoute(
            name = "Локации",
            route = Screens.LocationsListScreen.route,
            icon = ImageVector.vectorResource(R.drawable.ic_locations)
        )
    )

    val bottomBar = @Composable {
        AppBottomBar(
            navController = navController,
            topLevelRoutes = topLevelRoutes
        )
    }

    NavHost(
        navController = navController, startDestination = Screens.CharactersListScreen.route
    ) {
        composable(
            route = Screens.CharactersListScreen.route,
            content = {
                CharactersListScreen(
                    navController = navController,
                    bottomBar = bottomBar
                )
            })

        composable(
            route = Screens.EpisodesListScreen.route,
            content = {
                EpisodesListScreen(
                    navController = navController,
                    bottomBar = bottomBar
                )
            })

        composable(
            route = Screens.LocationsListScreen.route,
            content = {
                LocationsListScreen(
                    navController = navController,
                    bottomBar = bottomBar
                )
            })


        with(Screens.CharacterDetailScreen) {
            dialog(
                route = "$route/{$argName}",
                arguments = listOf(
                    navArgument(argName) {
                        type = NavType.IntType
                        nullable = false
                        defaultValue = -1
                    })
            ) { backStackEntry ->
                CharacterDetailScreen(
                    characterId = backStackEntry.arguments?.getInt(argName),
                    navController = navController,
                    modifier = Modifier
                )
            }
        }


        with(Screens.EpisodeDetailScreen) {
            dialog(
                route = "$route/{$argName}",
                arguments = listOf(
                    navArgument(argName) {
                        type = NavType.IntType
                        nullable = false
                        defaultValue = -1
                    }),
            ) { backStackEntry ->
                EpisodeDetailScreen(
                    episodeId = backStackEntry.arguments?.getInt(argName),
                    navController = navController
                )
            }
        }

        with(Screens.LocationDetailScreen) {
            composable(
                route = "$route/{$argName}",
                arguments = listOf(
                    navArgument(argName) {
                        type = NavType.IntType
                        nullable = false
                        defaultValue = -1
                    })
            ) { backStackEntry ->
                LocationDetailScreen(
                    locationId = backStackEntry.arguments?.getInt(argName),
                    navController = navController
                )
            }
        }


    }

}
