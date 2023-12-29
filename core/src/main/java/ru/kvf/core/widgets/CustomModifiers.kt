package ru.kvf.core.widgets

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.shimmer(
    color: Color = Color.White
): Modifier = composed {
    val shimmerColors = listOf(
        color.copy(alpha = 0.3f),
        color.copy(alpha = 0.5f),
        color.copy(alpha = 1.0f),
        color.copy(alpha = 0.5f),
        color.copy(alpha = 0.3f),
    )
    this.background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(x = 100f, y = 0.0f),
            end = Offset(x = 400f, y = 270f),
        )
    )
}
