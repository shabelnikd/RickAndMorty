package com.shabelnikd.rickandmorty.ui.core.base.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {

    Row(
        modifier = modifier
            .animationScale(
                itemKey = itemKey,
                animate = animate,
                animateInitialScale = animateInitialScale,
                animateTargetScale = animateTargetScale,
                durationMillis = durationMillis
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    )
    {
        content()
    }
}
