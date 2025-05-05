package com.shabelnikd.rickandmorty.ui.components.animations.lottie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.shabelnikd.rickandmorty.R

@Composable
fun DiceAnimation(
    modifier: Modifier = Modifier,
    onAnimationFinish: (Boolean) -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.dice))
    val progress by animateLottieCompositionAsState(
        composition = composition,
    )

    var isFinished by remember { mutableStateOf(false) }


    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { progress },
    )

    if (progress == 1f) {
        isFinished = true
    }

    onAnimationFinish(isFinished)

}