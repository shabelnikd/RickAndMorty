package com.shabelnikd.rickandmorty.ui.navigation


sealed class CharactersNav(val route: String) {
    object CharactersListScreen : CharactersNav(route = "characters_list_screen")
    object CharacterDetailScreen : CharactersNav(route = "characters_detail_screen")
}
