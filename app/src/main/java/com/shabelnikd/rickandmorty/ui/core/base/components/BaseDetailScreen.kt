package com.shabelnikd.rickandmorty.ui.core.base.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shabelnikd.rickandmorty.ui.base.BaseViewModel
import com.shabelnikd.rickandmorty.ui.components.LoadingIndicator
import com.shabelnikd.rickandmorty.ui.components.animations.animationAlphaScaleRotateY
import kotlinx.coroutines.delay

@Composable
fun <T> BaseDetailScreen(
    itemKey: Any,
    modifier: Modifier = Modifier,
    animate: Boolean = true,
    itemState: BaseViewModel.UiState<T>,
    progressDelayMillis: Long = 1200,
    content: @Composable ColumnScope.(T) -> Unit

) {
    val showProgress = remember(itemKey) { mutableStateOf(false) }

    LaunchedEffect(itemKey) {
        delay(progressDelayMillis)
        showProgress.value = true
    }


    when (itemState) {
        is BaseViewModel.UiState.Success<T> -> {
            val item = itemState.data

            Column(
                modifier = modifier
                    .animationAlphaScaleRotateY(
                        itemKey = itemKey,
                        animate = animate
                    )
                    .background(
                        Color.Transparent, shape = MaterialTheme.shapes.small
                    )
                    .padding(16.dp),

                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content.invoke(this@Column, item)
            }

        }

        is BaseViewModel.UiState.NotLoaded -> {
            if (showProgress.value) {
                Text("Не загружено")
            }
        }

        is BaseViewModel.UiState.Loading -> {
            if (showProgress.value) {
                LoadingIndicator()
            }

        }

        is BaseViewModel.UiState.Error -> {
            if (showProgress.value) {
                Text("Ошибка загрузки")
            }
        }
    }
}



