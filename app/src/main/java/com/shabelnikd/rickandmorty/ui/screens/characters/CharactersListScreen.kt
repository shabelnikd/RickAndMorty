package com.shabelnikd.rickandmorty.ui.screens.characters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.room.util.TableInfo
import com.shabelnikd.rickandmorty.domain.models.characters.CharacterWithFavoriteStatus
import com.shabelnikd.rickandmorty.ui.components.CharacterListItem
import com.shabelnikd.rickandmorty.ui.components.FavoritesBottomSheetContent
import com.shabelnikd.rickandmorty.ui.core.base.components.CenteredTopBar
import com.shabelnikd.rickandmorty.ui.core.base.components.ErrorMessage
import com.shabelnikd.rickandmorty.ui.core.base.components.RefreshableScaffoldPagingList
import com.shabelnikd.rickandmorty.ui.navigation.Screens
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersListScreen(navController: NavController) {

    val vm = koinViewModel<CharactersScreenVM>()
    val characters = vm.charactersPagingFlowWithFavoriteStatus.collectAsLazyPagingItems()

    val searchQuery by vm.searchQuery.collectAsStateWithLifecycle()

    val showFavoritesSheet by vm.showFavoritesSheet.collectAsStateWithLifecycle()
    val favoritesListState by vm.favoritesListState.collectAsStateWithLifecycle()

    var blur by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    val animateList = remember { mutableStateOf(true) }

    val scrollBehaviorTop = TopAppBarDefaults.enterAlwaysScrollBehavior()


    LaunchedEffect(currentRoute) {
        if (currentRoute == Screens.CharactersListScreen.route) {
            blur = false
        }
    }


    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            focusManager.clearFocus()
        }
    }

    val topBar = @Composable {
        Column {
            CenteredTopBar(
                text = "Персонажи", imageVector = Icons.Filled.Refresh, actions = {
                    IconButton(onClick = { vm.openFavoritesSheet() }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Показать избранных"
                        )
                    }
                },
                modifier = Modifier,
                scrollBehavior = scrollBehaviorTop
            ) {
                characters.refresh()
            }


            OutlinedTextField(
                value = searchQuery,
                onValueChange = { vm.updateSearchQuery(it) },
                placeholder = { Text("Поиск персонажей по имени") },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Иконка поиска")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
            )
        }

    }


    RefreshableScaffoldPagingList<CharacterWithFavoriteStatus>(
        blur = blur,
        topBar = {
            topBar()
        },
        navController = navController,
        items = characters,
        listState = listState,
        scrollBehaviorTop = scrollBehaviorTop,
        emptyListItem = {
            ErrorMessage("Персонажи не найдены")
        },
        fab = {
            FloatingActionButton(onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            }, shape = CircleShape) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = "Arrow Up"
                )
            }
        }) { item ->


        CharacterListItem(
            animate = animateList.value,
            character = item,
            onToggleFavorite = { id, isFavorite ->
                vm.toggleFavoriteStatus(item.characterModel.id, isFavorite)
            }, modifier = Modifier.fillMaxWidth()
        ) {
            with(Screens.CharacterDetailScreen) {
                blur = true
                navController.navigate(route = "$route/${item.characterModel.id}")
            }
        }


        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.6.dp)
    }


    val sheetState = rememberModalBottomSheetState()
    val coroutineScopeForSheet = rememberCoroutineScope()


    if (showFavoritesSheet) {
        ModalBottomSheet(
            onDismissRequest = { vm.closeFavoritesSheet() }, sheetState = sheetState
        ) {
            FavoritesBottomSheetContent(
                listState = favoritesListState,

                onCharacterClick = { characterId ->
                    coroutineScopeForSheet.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            vm.closeFavoritesSheet()

                            with(Screens.CharacterDetailScreen) {
                                blur = true
                                navController.navigate(route = "$route/$characterId")
                            }
                        }
                    }
                },
                onClickRetry = { vm.retryFavoritesLoad() },
                onToggleFavorite = { characterId, status ->
                    vm.toggleFavoriteStatus(characterId = characterId, isCurrentlyFavorite = true)
                })

        }
    }
}