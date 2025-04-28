package com.shabelnikd.rickandmorty.ui.core.base.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun StyledTextKeyValue(
    modifier: Modifier,
    key: String,
    value: String,
    color: Color
) {
    val brush = Brush.linearGradient(listOf<Color>(Color.Transparent, color))

    Column(
        modifier = modifier,
    ) {
        Text(
            text = key,
            style = MaterialTheme.typography.labelSmall,
            color = color.copy(alpha = 0.7f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.padding(2.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = color,
            )
        }

        Spacer(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .size(0.6.dp)
                .background(brush = brush)
        )
    }
}


@Composable
fun StyledTextKeyValueList(
    color: Color = MaterialTheme.colorScheme.onBackground,
    filterValue: String = "",
    vararg keyValuePair: Pair<String, String>
) {
    keyValuePair
        .filter { it.second != filterValue && it.first != filterValue }
        .forEachIndexed { index, entry ->
            val key = entry.first
            val value = entry.second

            val alpha = remember(index) { Animatable(0f) }
            val scale = remember(index) { Animatable(0.5f) }

            LaunchedEffect(index) {

                val staggerDelay = 60L
                delay(index * staggerDelay)

                launch {
                    delay(300)
                    alpha.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 400)
                    )
                }

                launch {
                    delay(300)
                    scale.animateTo(
                        targetValue = 1.01f,
                        animationSpec = tween(durationMillis = 400)
                    )

                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 400)
                    )
                }

            }

            StyledTextKeyValue(
                modifier = Modifier
                    .alpha(alpha.value)
                    .graphicsLayer {
                        this.scaleX = scale.value
                        this.scaleY = scale.value
                        this.compositingStrategy = CompositingStrategy.Auto
                    }, key = key, value = value, color = color
            )
        }
}


