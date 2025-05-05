package com.shabelnikd.rickandmorty.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shabelnikd.rickandmorty.domain.models.characters.CharacterWithFavoriteStatus
import com.shabelnikd.rickandmorty.ui.base.BaseViewModel.UiState
import com.shabelnikd.rickandmorty.ui.core.base.components.ErrorMessage


@Composable
fun FavoritesBottomSheetContent(
    listState: UiState<List<CharacterWithFavoriteStatus>>,
    onCharacterClick: (Int) -> Unit,
    onToggleFavorite: (Int, Boolean) -> Unit,
    onClickRetry: () -> Unit
) {

    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
    ) {
        when (listState) {
            is UiState.Loading -> {
                LoadingIndicator(modifier = Modifier.fillMaxSize())
            }

            is UiState.Success<List<CharacterWithFavoriteStatus>> -> {
                val favorites = listState.data

                if (favorites.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Список избранного пуст")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        state = lazyListState,

                        ) {
                        items(
                            count = favorites.size
                        ) { index ->
                            val favoriteCharacterWithStatus = favorites[index]

                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { dismissalValue ->
                                    onToggleFavorite(
                                        favoriteCharacterWithStatus.characterModel.id,
                                        favoriteCharacterWithStatus.isFavorite
                                    )
                                    true
                                }
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromStartToEnd = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem(),

                                backgroundContent = {},

                                content = {
                                    CharacterListItem(
                                        character = favoriteCharacterWithStatus,
                                        onClick = { onCharacterClick(favoriteCharacterWithStatus.characterModel.id) },
                                        onToggleFavorite = onToggleFavorite,
                                        animate = false,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .animateItem()
                                    )
                                },
                            )

                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 0.6.dp
                            )
                        }
                    }
                }
            }

            is UiState.Error -> {
                val errorState = listState
                ErrorMessage(
                    message = "Ошибка загрузки избранного: ${errorState.message}",
                    modifier = Modifier.fillMaxSize(),
                    onClickRetry = onClickRetry
                )
            }

            is UiState.NotLoaded -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Ожидание загрузки избранного...")
                }
            }
        }
    }
}