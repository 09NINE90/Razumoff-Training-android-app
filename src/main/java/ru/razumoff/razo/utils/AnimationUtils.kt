package ru.razumoff.razo.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith

fun AnimatedContentTransitionScope<Boolean>.profileContentTransition():
        ContentTransform {
    return if (targetState) {
        (
                fadeIn(tween(200)) +
                        slideInVertically(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMediumLow
                            ),
                            initialOffsetY = { it / 6 }
                        )
                ) togetherWith
                fadeOut(tween(150))
    } else {
        (
                fadeIn(tween(200)) +
                        slideInVertically(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMediumLow
                            ),
                            initialOffsetY = { -it / 6 }
                        )
                ) togetherWith
                fadeOut(tween(150))
    }
}

fun enterFadeInExpandVerticallyTransition(): EnterTransition {
    return fadeIn(
        animationSpec = tween(200)
    ) + expandVertically(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )
}

fun exitFadeOutShrinkVerticallyTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(150)
    ) + shrinkVertically(
        animationSpec = tween(150)
    )
}