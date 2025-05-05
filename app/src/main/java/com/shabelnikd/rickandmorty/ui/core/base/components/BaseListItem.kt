package com.shabelnikd.rickandmorty.ui.core.base.components

import android.content.Context
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.shabelnikd.rickandmorty.ui.components.animations.animationScale

@Composable
fun BaseListItem(
    itemKey: Any,
    modifier: Modifier = Modifier,
    animate: Boolean = true,
    animateInitialScale: Float = 0.9f,
    animateTargetScale: Float = 1.15f,
    durationMillis: Int = 400,
    delayMillis: Long = 0L,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager

    Row(
        modifier = modifier
            .animationScale(
                itemKey = itemKey,
                animate = animate,
                delayMillis = delayMillis,
                animateInitialScale = animateInitialScale,
                animateTargetScale = animateTargetScale,
                durationMillis = durationMillis
            )
            .clickable(onClick = {
                vibrator.defaultVibrator.vibrate(
                    VibrationEffect.createOneShot(500L, VibrationEffect.DEFAULT_AMPLITUDE)
                )
                onClick()
            })
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    )
    {
        content()
    }
}
