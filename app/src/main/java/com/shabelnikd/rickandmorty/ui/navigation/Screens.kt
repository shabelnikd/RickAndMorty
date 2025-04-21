package com.shabelnikd.rickandmorty.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector


sealed class Screens(val route: String, val argName: String? = null) {

    object CharactersListScreen : Screens(route = "characters_list_screen")
    object CharacterDetailScreen :
        Screens(route = "character_detail_screen", argName = "characterId")

    object EpisodesListScreen : Screens(route = "episodes_list_screen")
    object EpisodeDetailScreen :
        Screens(route = "episode_detail_screen", argName = "episodeId")

    object LocationsListScreen : Screens(route = "locations_list_screen")
    object LocationDetailScreen :
        Screens(route = "location_detail_screen", argName = "locationId")
}

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)


