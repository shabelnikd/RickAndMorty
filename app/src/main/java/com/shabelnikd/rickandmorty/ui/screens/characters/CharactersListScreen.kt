package com.shabelnikd.rickandmorty.ui.screens.characters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.shabelnikd.rickandmorty.ui.components.CenteredTopBar
import com.shabelnikd.rickandmorty.ui.components.CharacterListItem
import com.shabelnikd.rickandmorty.ui.navigation.Screens
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharactersListScreen(navController: NavController, bottomAppBar: @Composable () -> Unit) {
    val vm = koinViewModel<CharactersScreenVM>()
    val characters = vm.charactersPagingFlow.collectAsLazyPagingItems()


    Scaffold(
        topBar = {
            CenteredTopBar(
                text = "Персонажи",
            ) {
                navController.navigateUp()
            }
        },
        bottomBar = bottomAppBar
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn {
                items(characters.itemCount) { index ->
                    val character = characters[index]
                    character?.let {
                        CharacterListItem(it) {
                            with(Screens.CharacterDetailScreen) {
                                navController.navigate(route = route + "?$argName=${it.id}")
                            }
                        }
                    }
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                }
            }
        }

    }


}