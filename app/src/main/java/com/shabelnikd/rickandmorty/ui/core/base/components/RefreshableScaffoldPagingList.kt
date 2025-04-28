package com.shabelnikd.rickandmorty.ui.core.base.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.shabelnikd.rickandmorty.ui.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> RefreshableScaffoldPagingList(
    modifier: Modifier = Modifier,
    blur: Boolean = false,
    topBar: @Composable () -> Unit = {},
    navController: NavController,
    scrollBehaviorTop: TopAppBarScrollBehavior,
    listState: LazyListState,
    fab: @Composable () -> Unit = {},
    emptyListItem: @Composable () -> Unit,
    items: LazyPagingItems<T>,
    itemsContent: @Composable LazyItemScope.(item: T) -> Unit,
) {

    val pullRefreshState = rememberPullToRefreshState()
    val scrollBehaviorBottom = BottomAppBarDefaults.exitAlwaysScrollBehavior()


    val fabScaleInAnimationSpec: FiniteAnimationSpec<Float> = keyframes {
        durationMillis = 2000
        0.5f at 0
        1.15f at 400
        1f at 800

    }

    val fabEnterTransition: EnterTransition =
        fadeIn(animationSpec = tween(durationMillis = 400)) + scaleIn(
            animationSpec = fabScaleInAnimationSpec, initialScale = 0.5f
        ) + slideInVertically(
            initialOffsetY = { it / 2 }, animationSpec = tween(600)
        )


    val fabExitTransition: ExitTransition =
        fadeOut(animationSpec = tween(durationMillis = 600))


    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehaviorBottom.nestedScrollConnection)
            .nestedScroll(scrollBehaviorTop.nestedScrollConnection)
            .then(
                if (blur) {
                    Modifier.blur(radius = 20.dp, edgeTreatment = BlurredEdgeTreatment.Rectangle)
                } else {
                    Modifier
                }
            ),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = topBar,
        bottomBar = {
            BottomNavBar(
                navController = navController,
                scrollBehavior = scrollBehaviorBottom
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = listState.canScrollBackward,
                enter = fabEnterTransition,
                exit = fabExitTransition
            ) {
                fab()
            }

        }
    ) { innerPadding ->

        PullToRefreshBox(
            modifier = Modifier
                .padding(innerPadding),
            isRefreshing = items.loadState.refresh is LoadState.Loading,
            onRefresh = { items.refresh() },
            state = pullRefreshState
        ) {
            Column(modifier = Modifier) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(), state = listState
                ) {
                    when (items.loadState.refresh) {
                        is LoadState.Error -> {
                            val e = items.loadState.refresh as LoadState.Error
                            item {
                                ErrorMessage(
                                    message = "Ошибка при загрузке списка: ${e.error.localizedMessage ?: "Неизвестная ошибка"}",
                                    modifier = Modifier.fillParentMaxSize(),
                                    onClickRetry = { items.retry() })
                            }
                        }

                        else -> {}
                    }


                    items(count = items.itemCount) { index ->
                        val item = items[index]

                        item?.let {
                            itemsContent.invoke(this, item)
                        }

                    }


                    when (items.loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                LoadingIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }

                        is LoadState.Error -> {
                            val e = items.loadState.append as LoadState.Error
                            item {
                                ErrorMessage(
                                    message = "Ошибка при загрузке следующей страницы: ${e.error.localizedMessage ?: "Неизвестная ошибка"}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    onClickRetry = { items.retry() })
                            }
                        }

                        else -> {}
                    }

                    if (items.loadState.refresh is LoadState.NotLoading && items.loadState.append is LoadState.NotLoading && items.itemCount == 0) {
                        item {
                            emptyListItem()
                        }
                    }

                }
            }
        }

    }


}