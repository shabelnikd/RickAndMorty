package com.shabelnikd.rickandmorty.ui.screens.locations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.paging.compose.collectAsLazyPagingItems
import com.shabelnikd.rickandmorty.ui.components.CharacterListItem
import com.shabelnikd.rickandmorty.ui.navigation.CharactersNav
import com.shabelnikd.rickandmorty.ui.vm.characters.CharactersScreenVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocationsListScreen(navController: NavController) {
    val vm = koinViewModel<CharactersScreenVM>()
    val characters = vm.charactersPagingFlow.collectAsLazyPagingItems()


    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn {
                items(characters.itemCount) { index ->
                    val character = characters[index]
                    character?.let {
                        CharacterListItem(it) {
                            with(CharactersNav.CharacterDetailScreen) {
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