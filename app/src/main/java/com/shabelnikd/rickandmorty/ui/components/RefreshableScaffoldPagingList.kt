package com.shabelnikd.rickandmorty.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> RefreshableScaffoldPagingList(
    modifier: Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    listState: LazyListState,
    fab: @Composable () -> Unit = {},
    emptyListItem: @Composable () -> Unit,
    items: LazyPagingItems<T>,
    itemsContent: @Composable (item: T) -> Unit,


    ) {
    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        modifier = modifier,
        topBar = topBar, bottomBar = bottomBar,
        floatingActionButton = fab
    ) { innerPadding ->

        PullToRefreshBox(
            modifier = Modifier.padding(innerPadding),
            isRefreshing = items.loadState.refresh is LoadState.Loading,
            onRefresh = { items.refresh() },
            state = pullRefreshState
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
                when (items.loadState.refresh) {
                    is LoadState.Error -> {
                        val e = items.loadState.refresh as LoadState.Error
                        item {
                            ErrorItem(
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
                        itemsContent.invoke(item)
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
                            ErrorItem(
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