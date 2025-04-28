package com.shabelnikd.rickandmorty.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shabelnikd.rickandmorty.ui.screens.characters.CharactersListScreen
import com.shabelnikd.rickandmorty.ui.screens.characters.detail.CharacterDetailScreen
import com.shabelnikd.rickandmorty.ui.screens.episodes.EpisodesListScreen
import com.shabelnikd.rickandmorty.ui.screens.episodes.detail.EpisodeDetailScreen
import com.shabelnikd.rickandmorty.ui.screens.locations.LocationsListScreen
import com.shabelnikd.rickandmorty.ui.screens.locations.detail.LocationDetailScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationStack() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.CharactersListScreen.route
    ) {
        composable(
            route = Screens.CharactersListScreen.route,
            content = {
                CharactersListScreen(
                    navController = navController
                )
            })

        composable(
            route = Screens.EpisodesListScreen.route,
            content = {
                EpisodesListScreen(
                    navController = navController,
                )
            })

        composable(
            route = Screens.LocationsListScreen.route,
            content = {
                LocationsListScreen(
                    navController = navController,
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
                    characterId = backStackEntry.arguments?.getInt(argName) ?: -1,
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
                    episodeId = backStackEntry.arguments?.getInt(argName) ?: -1,
                    navController = navController
                )
            }
        }

        with(Screens.LocationDetailScreen) {
            dialog(
                route = "$route/{$argName}",
                arguments = listOf(
                    navArgument(argName) {
                        type = NavType.IntType
                        nullable = false
                        defaultValue = -1
                    })
            ) { backStackEntry ->
                LocationDetailScreen(
                    locationId = backStackEntry.arguments?.getInt(argName) ?: -1,
                    navController = navController
                )
            }
        }


    }

}
