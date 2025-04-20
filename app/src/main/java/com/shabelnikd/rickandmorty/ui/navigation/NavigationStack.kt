package com.shabelnikd.rickandmorty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shabelnikd.rickandmorty.ui.screens.characters.CharacterDetailScreen
import com.shabelnikd.rickandmorty.ui.screens.characters.CharactersListScreen

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = CharactersNav.CharactersListScreen.route
    ) {
        composable(route = CharactersNav.CharactersListScreen.route) {
            CharactersListScreen(navController = navController)
        }

        composable(
            route = CharactersNav.CharacterDetailScreen.route + "?characterId={characterId}",
            arguments = listOf(
                navArgument("characterId") {
                    type = NavType.IntType
                    nullable = false
                })
        ) { backStackEntry ->
            CharacterDetailScreen(
                characterId = backStackEntry.arguments?.getInt("characterId") ?: 0
            )
        }

    }

}
