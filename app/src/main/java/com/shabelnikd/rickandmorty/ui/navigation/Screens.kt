package com.shabelnikd.rickandmorty.ui.navigation


sealed class CharactersNav(val route: String, val argName: String? = null) {
    object CharactersListScreen : CharactersNav(route = "characters_list_screen")
    object CharacterDetailScreen :
        CharactersNav(route = "characters_detail_screen", argName = "characterId")
}
