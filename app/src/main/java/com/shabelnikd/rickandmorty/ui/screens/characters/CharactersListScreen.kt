package com.shabelnikd.rickandmorty.ui.screens.characters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.compose.collectAsLazyPagingItems
import com.shabelnikd.rickandmorty.domain.models.characters.Character
import com.shabelnikd.rickandmorty.ui.components.CenteredTopBar
import com.shabelnikd.rickandmorty.ui.components.CharacterListItem
import com.shabelnikd.rickandmorty.ui.components.RefreshableScaffoldPagingList
import com.shabelnikd.rickandmorty.ui.navigation.Screens
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersListScreen(navController: NavController, bottomBar: @Composable () -> Unit) {

    val vm = koinViewModel<CharactersScreenVM>()
    val characters = vm.charactersPagingFlow.collectAsLazyPagingItems()

    var blur by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val density = LocalDensity.current.density

    LaunchedEffect(currentRoute) {
        if (currentRoute == Screens.CharactersListScreen.route) {
            blur = false
        }
    }

    val topBar = @Composable {
        CenteredTopBar(
            text = "Персонажи", imageVector = Icons.Filled.Refresh
        ) {
            characters.refresh()
        }
    }

    val emptyListItem = @Composable {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text("Персонажи не найдены")
        }
    }


    RefreshableScaffoldPagingList<Character>(
        modifier = when (blur) {
            true -> Modifier.blur(radius = 20.dp, edgeTreatment = BlurredEdgeTreatment.Rectangle)
            else -> Modifier
        },
        topBar = topBar,
        bottomBar = bottomBar,
        emptyListItem = emptyListItem,
        items = characters,
        itemsContent = { item ->

            val alpha = remember(item.id) { Animatable(0f) }
            val translationX = remember(item.id) { Animatable(-100f * density) }

            LaunchedEffect(item.id) {
                launch {
                    alpha.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
                launch {
                    translationX.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 400)
                    )
                }
            }

            CharacterListItem(
                character = item,
                modifier = Modifier
                    .alpha(alpha.value)
                    .graphicsLayer { this.translationX = translationX.value }
                    .fillMaxWidth()
            ) {
                with(Screens.CharacterDetailScreen) {
                    blur = true
                    navController.navigate(route = "$route/${item.id}")
                }
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.6.dp)
        },
        listState = listState,
        fab = {
            AnimatedVisibility(
                visible = listState.canScrollBackward,
                enter = fadeIn(animationSpec = tween(2000)),
                exit = fadeOut()
            ) {
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                }, shape = CircleShape) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Arrow Up"
                    )
                }
            }
        }
    )

}