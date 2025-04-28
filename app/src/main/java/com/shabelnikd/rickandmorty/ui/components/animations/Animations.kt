package com.shabelnikd.rickandmorty.ui.components.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.launch

/**
 * Модификатор, применяющий анимацию масштабирования по типу (0.9 -> 1.15 -> 1) при появлении элемента.
 *
 * @param itemKey Уникальный ключ элемента (например, ID). Используется для привязки состояния анимации.
 * @param animate Флаг, определяющий, нужно ли запускать анимацию.
 * @param animateInitialScale Начальное значение масштаба элемента
 * @param animateTargetScale Промежуточное значение масштаба элемента
 * @param durationMillis Время анимации
 *
 * @sample com.shabelnikd.rickandmorty.ui.core.base.components.BaseListItem
 *
 */
fun Modifier.animationScale(
    itemKey: Any,
    animate: Boolean = true,
    animateInitialScale: Float = 0.95f,
    animateTargetScale: Float = 1.15f,
    durationMillis: Int = 400,
): Modifier = composed {
    val scale = remember(itemKey) { Animatable(animateInitialScale) }

    LaunchedEffect(itemKey) {
        if (animate) {
            launch {
                scale.animateTo(
                    targetValue = animateTargetScale,
                    animationSpec = tween(durationMillis = durationMillis)
                )

                scale.animateTo(
                    targetValue = 1f, animationSpec = tween(durationMillis = durationMillis)
                )
            }
        }
    }

    this.then(
        if (animate) {
            this.graphicsLayer {
                this.scaleX = scale.value
                this.scaleY = scale.value
            }
        } else {
            this
        }
    )
}

/**
 * Модификатор, применяющий анимацию масштабирования и поворота при появлении элемента.
 *
 * @param itemKey Уникальный ключ элемента (например, ID). Используется для привязки состояния анимации.
 * @param animate Флаг, определяющий, нужно ли запускать анимацию.
 * @param animateInitialScale Начальное значение масштаба элемента
 * @param animateInitialAlpha Начальное значение прозрачности масштаба элемента
 * @param animateInitialDegreeY Начальное значение координаты Y
 * @param rotateDurationMillis Время анимации поворота
 * @param scaleDurationMillis Время анимации масштаба
 * @param alphaDurationMillis Время анимации прозрачности
 *
 */
fun Modifier.animationAlphaScaleRotateY(
    itemKey: Any,
    animate: Boolean = true,
    animateInitialScale: Float = 0.95f,
    animateInitialAlpha: Float = 0f,
    animateInitialDegreeY: Float = 90f,
    rotateDurationMillis: Int = 800,
    scaleDurationMillis: Int = 400,
    alphaDurationMillis: Int = 400,
): Modifier = composed {
    val imageAlpha = remember(itemKey) { Animatable(animateInitialAlpha) }
    val imageScale = remember(itemKey) { Animatable(animateInitialScale) }
    val rotate = remember(itemKey) { Animatable(animateInitialDegreeY) }

    if (animate) {
        LaunchedEffect(itemKey) {
            launch {
                rotate.animateTo(
                    targetValue = 0f, animationSpec = tween(durationMillis = rotateDurationMillis)
                )
            }

            launch {
                imageAlpha.animateTo(
                    targetValue = 1f, animationSpec = tween(durationMillis = alphaDurationMillis)
                )
            }

            launch {
                imageScale.animateTo(
                    targetValue = 1f, animationSpec = tween(durationMillis = scaleDurationMillis)
                )
            }
        }
    }

    this.then(
        if (animate) {
            Modifier.graphicsLayer {
                this.alpha = imageAlpha.value
                this.cameraDistance = 100f
                this.rotationY = rotate.value
                this.scaleX = imageScale.value
                this.scaleY = imageScale.value
            }
        } else {
            Modifier
        }
    )

}