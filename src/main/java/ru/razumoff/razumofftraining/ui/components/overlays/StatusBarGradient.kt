package ru.razumoff.razumofftraining.ui.components.overlays

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatusBarGradient(
    modifier: Modifier = Modifier,
    gradientHeight: Int = 80
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(gradientHeight.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.5f),
                        Color.Transparent
                    ),
                    startY = 0f,
                    endY = 1f
                )
            )
    )
}

@Composable
fun NavigationBarGradient(
    modifier: Modifier = Modifier,
    gradientHeight: Int = 80
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(gradientHeight.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.5f)
                    ),
                    startY = 0f,
                    endY = 1f
                )
            )
    )
}