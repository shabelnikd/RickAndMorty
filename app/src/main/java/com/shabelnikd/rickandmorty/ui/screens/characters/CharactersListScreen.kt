package com.shabelnikd.rickandmorty.ui.screens.characters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.compose.collectAsLazyPagingItems
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.shabelnikd.rickandmorty.R
import com.shabelnikd.rickandmorty.domain.models.characters.CharacterWithFavoriteStatus
import com.shabelnikd.rickandmorty.ui.components.CharacterFilterSheetContent
import com.shabelnikd.rickandmorty.ui.components.CharacterListItem
import com.shabelnikd.rickandmorty.ui.components.FavoritesBottomSheetContent
import com.shabelnikd.rickandmorty.ui.core.base.components.RefreshableScaffoldPagingList
import com.shabelnikd.rickandmorty.ui.core.base.components.TopBar
import com.shabelnikd.rickandmorty.ui.navigation.Screens
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random
import kotlin.random.nextInt


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CharactersListScreen(navController: NavController) {

    val vm = koinViewModel<CharactersListScreenVM>()

    val focusManager = LocalFocusManager.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val density = LocalDensity.current

    val characters = vm.charactersPagingFlowWithFavoriteStatus.collectAsLazyPagingItems()
    val favoritesListState by vm.favoritesListState.collectAsStateWithLifecycle()
    val totalCharacterCount by vm.totalCharacterCount.collectAsStateWithLifecycle()

    val showFavoritesSheet by vm.showFavoritesSheet.collectAsStateWithLifecycle()
    val showFilterSheet by vm.showFilterSheet.collectAsStateWithLifecycle()

    val sheetStateFilters = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScopeForSheetFilters = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(

    )
    val coroutineScopeForSheet = rememberCoroutineScope()

    val searchQuery by vm.searchQuery.collectAsStateWithLifecycle()
    val statusFilter by vm.statusFilter.collectAsStateWithLifecycle()
    val genderFilter by vm.genderFilter.collectAsStateWithLifecycle()
    val speciesFilter by vm.speciesFilter.collectAsStateWithLifecycle()
    val typeFilter by vm.typeFilter.collectAsStateWithLifecycle()

    var blurEnabled by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    val animateList = remember { mutableStateOf(true) }

    var scaffoldPaddingValues by remember { mutableStateOf(PaddingValues(0.dp)) }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.dice))

    var isDiceShow by remember { mutableStateOf(false) }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isDiceShow,
        iterations = 1,
        ignoreSystemAnimatorScale = true
    )

    val scrollBehaviorTop = TopAppBarDefaults.enterAlwaysScrollBehavior()


    LaunchedEffect(currentRoute) {
        if (currentRoute == Screens.CharactersListScreen.route) {
            blurEnabled = false
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(isDiceShow, progress) {
        if (isDiceShow && progress == 1f) {
            with(Screens.CharacterDetailScreen) {
                navController.navigate(
                    route = "$route/${Random.nextInt(1..totalCharacterCount)}"
                )
            }
            isDiceShow = false
        }
    }

    val topBar = @Composable
    fun(modifier: Modifier) {
        Column {
            TopBar(
                text = "Персонажи",
                imageVector = Icons.Filled.Refresh,
                modifier = modifier,
                actions = {
                    val favoriteVector = ImageVector.vectorResource(R.drawable.ic_favorite_unfilled)
                    val casinoVector = ImageVector.vectorResource(R.drawable.ic_casino)
                    val tuneVector = ImageVector.vectorResource(R.drawable.ic_tune)

                    val defaultAction = {
                        focusManager.clearFocus()
                        blurEnabled = true
                    }

                    IconButton(onClick = {
                        defaultAction()
                        vm.openFavoritesSheet()
                    }) {
                        Icon(imageVector = favoriteVector, contentDescription = "favorites")
                    }


                    IconButton(onClick = {
                        defaultAction()
                        isDiceShow = true
                    }) {
                        Icon(imageVector = casinoVector, contentDescription = "random")
                    }


                    IconButton(onClick = {
                        defaultAction()
                        vm.openFilterSheet()
                    }) {
                        Icon(imageVector = tuneVector, contentDescription = "filters")
                    }
                },

                onBackClick = { characters.refresh() },

                scrollBehavior = scrollBehaviorTop
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { value -> vm.updateSearchQuery(value) },
                placeholder = { Text("Поиск персонажей по имени") },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
            )
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        RefreshableScaffoldPagingList<CharacterWithFavoriteStatus>(
            navController = navController,
            blurEnabled = blurEnabled,
            topBar = { modifier ->
                topBar(modifier)
            },
            modifier = Modifier
                .nestedScroll(scrollBehaviorTop.nestedScrollConnection),

            items = characters,
            listState = listState,
            getPaddingValues = { paddingValues ->
                scaffoldPaddingValues = paddingValues
            },
        ) { item ->
            CharacterListItem(
                animate = animateList.value,
                character = item,
                onToggleFavorite = { id, isFavorite ->
                    vm.toggleFavoriteStatus(item.characterModel.id, isFavorite)
                }, modifier = Modifier.fillMaxWidth()
            ) {
                with(Screens.CharacterDetailScreen) {
                    blurEnabled = true
                    navController.navigate(route = "$route/${item.characterModel.id}")
                }
            }
        }

        AnimatedVisibility(
            visible = isDiceShow,
        ) {
            Column {
                CenterAlignedTopAppBar(
                    modifier = Modifier
                        .padding(top = scaffoldPaddingValues.calculateTopPadding()),
                    title = { Text("Кто ты из Rick & Morty?") },
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = Color.Transparent
                    )
                )
                LottieAnimation(composition = composition)
            }
        }
    }



    if (showFavoritesSheet) {
        ModalBottomSheet(
//            modifier = Modifier.padding(top = ScaffoldDefaults.contentWindowInsets.getTop(density).dp),
            onDismissRequest = {
                blurEnabled = false
                vm.closeFavoritesSheet()
            },
            sheetState = sheetState
        ) {
            FavoritesBottomSheetContent(
                listState = favoritesListState,
                onCharacterClick = { characterId ->
                    coroutineScopeForSheet.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            vm.closeFavoritesSheet()
                            with(Screens.CharacterDetailScreen) {
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


    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                blurEnabled = false
                vm.closeFilterSheet()
            },
            sheetState = sheetStateFilters
        ) {
            CharacterFilterSheetContent(
                status = statusFilter,
                gender = genderFilter,
                species = speciesFilter,
                type = typeFilter,
                onStatusChange = { vm.updateStatusFilter(it) },
                onGenderChange = { vm.updateGenderFilter(it) },
                onSpeciesChange = { vm.updateSpeciesFilter(it) },
                onTypeChange = { vm.updateTypeFilter(it) },
                onCloseSheet = {
                    coroutineScopeForSheetFilters.launch {
                        sheetStateFilters.hide()
                    }.invokeOnCompletion {
                        if (!sheetStateFilters.isVisible) {
                            vm.closeFilterSheet()
                        }
                    }
                }
            )
        }
    }
}