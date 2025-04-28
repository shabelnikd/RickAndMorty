package com.shabelnikd.rickandmorty.ui.navigation

import com.shabelnikd.rickandmorty.R


sealed class Screens(val route: String, val argName: String = "") {

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


data class TopLevelRoute<T : Any>(val name: String, val route: T, val iconResId: Int) {

    companion object {
        val TopLevelRoutes = listOf<TopLevelRoute<String>>(
            TopLevelRoute(
                name = "Персонажи",
                route = Screens.CharactersListScreen.route,
                iconResId = R.drawable.ic_characters
            ),
            TopLevelRoute(
                name = "Эпизоды",
                route = Screens.EpisodesListScreen.route,
                iconResId = R.drawable.ic_episodes
            ),
            TopLevelRoute(
                name = "Локации",
                route = Screens.LocationsListScreen.route,
                iconResId = R.drawable.ic_locations
            )
        )
    }
}




