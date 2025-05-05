package com.shabelnikd.rickandmorty.ui.core.base.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.shabelnikd.rickandmorty.ui.components.FabScrollTop
import com.shabelnikd.rickandmorty.ui.components.ShimmeringCharacterPlaceholder
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeLogger
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials


@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun <T : Any> RefreshableScaffoldPagingList(
    modifier: Modifier = Modifier,
    topBar: @Composable (Modifier) -> Unit = {},
    navController: NavController,
    blurEnabled: Boolean = HazeDefaults.blurEnabled(),
    listState: LazyListState,
    shimmerItem: (@Composable (Boolean) -> Unit)? = null,
    getPaddingValues: (PaddingValues) -> Unit = {},
    items: LazyPagingItems<T>,
    itemsContent: @Composable LazyItemScope.(item: T) -> Unit,
) {

    val pullRefreshState = rememberPullToRefreshState()
    val overscrollEffect = rememberOverscrollEffect()
    val scrollBehaviorBottom = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    val hazeStyle = HazeMaterials.ultraThin(MaterialTheme.colorScheme.surface).copy(
        blurRadius = 16.dp,
    )

    val dividerBrush = Brush.linearGradient(
        colors = listOf<Color>(
            Color.Transparent,
            DividerDefaults.color
        )
    )

    val bottomBar = @Composable
    fun(modifier: Modifier) {
        BottomNavBar(
            navController = navController,
            scrollBehavior = scrollBehaviorBottom,
            modifier = modifier
        )
    }

    val pullRefreshIndicator = @Composable {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            PullToRefreshDefaults.Indicator(
                state = pullRefreshState,
                isRefreshing = items.loadState.refresh is LoadState.Loading,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            LazyColumn {
                items(count = 10) {
                    shimmerItem?.let {
                        it(items.loadState.refresh is LoadState.Loading)
                    } ?: run {
                        ShimmeringCharacterPlaceholder(items.loadState.refresh is LoadState.Loading)

                    }
                }
            }
        }
    }

    HazeLogger.enabled = true

    Scaffold(
        modifier = modifier
            .then(
                if (blurEnabled) {
                    Modifier.hazeEffect(style = hazeStyle)
                } else {
                    Modifier
                }
            )
            .nestedScroll(scrollBehaviorBottom.nestedScrollConnection),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = { topBar(Modifier) },
        bottomBar = { bottomBar(Modifier) },
        floatingActionButton = { FabScrollTop(listState = listState) }
    ) { innerPaddingValues ->

        getPaddingValues(innerPaddingValues)

        PullToRefreshBox(
            modifier = Modifier
                .padding(innerPaddingValues),
            isRefreshing = items.loadState.refresh is LoadState.Loading,
            indicator = { pullRefreshIndicator() },
            onRefresh = { items.refresh() },
            state = pullRefreshState
        ) {
            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = listState,
                    overscrollEffect = overscrollEffect
                ) {
                    with(items.loadState) {
                        when (val e = refresh) {
                            is LoadState.Error -> {
                                item {
                                    ErrorMessage(
                                        message = e.error.localizedMessage ?: "Неизвестная ошибка",
                                        modifier = Modifier.fillParentMaxSize(),
                                        onClickRetry = { items.retry() })
                                }
                            }

                            else -> {}
                        }

                        when (val e = append) {
                            is LoadState.Error -> {
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

                        if (refresh is LoadState.NotLoading && append is LoadState.NotLoading && items.itemCount == 0) {
                            item {
                                ErrorMessage(
                                    message = "Ничего не найдено",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    onClickRetry = { items.retry() })
                            }
                        }

                        items(count = items.itemCount) { index ->
                            items[index]?.let {
                                itemsContent.invoke(this@items, it)
                            }

                            Spacer(
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .fillMaxWidth()
                                    .size(0.6.dp)
                                    .background(brush = dividerBrush)
                            )
                        }
                    }
                }
            }
        }
    }
}