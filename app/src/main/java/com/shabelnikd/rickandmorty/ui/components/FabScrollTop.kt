package com.shabelnikd.rickandmorty.ui.components

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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch

@Composable
fun FabScrollTop(
    listState: LazyListState,
    imageVector: ImageVector = Icons.Filled.KeyboardArrowUp,

    ) {
    val coroutineScope = rememberCoroutineScope()

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

    AnimatedVisibility(
        visible = listState.canScrollBackward,
        enter = fabEnterTransition,
        exit = fabExitTransition
    ) {
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            },
            shape = CircleShape
        ) {
            Icon(
                imageVector = imageVector, contentDescription = "List to top"
            )
        }
    }

}