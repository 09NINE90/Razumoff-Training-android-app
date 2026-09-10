package ru.razumoff.razumofftraining.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

fun AnimatedContentTransitionScope<*>.slideFadeEnter(): EnterTransition =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
    ) + fadeIn(
        animationSpec = tween(300)
    )

fun AnimatedContentTransitionScope<*>.slideFadeExit(): ExitTransition =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
    ) + fadeOut(
        animationSpec = tween(200)
    )

fun AnimatedContentTransitionScope<*>.slideFadePopEnter(): EnterTransition =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
    ) + fadeIn(
        animationSpec = tween(300)
    )

fun AnimatedContentTransitionScope<*>.slideFadePopExit(): ExitTransition =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
    ) + fadeOut(
        animationSpec = tween(200)
    )


fun AnimatedContentTransitionScope<*>.detailEnter(): EnterTransition =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(300)
    ) + fadeIn(
        animationSpec = tween(300)
    )

fun AnimatedContentTransitionScope<*>.detailExit(): ExitTransition =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(300)
    ) + fadeOut(
        animationSpec = tween(200)
    )

fun AnimatedContentTransitionScope<*>.detailPopEnter(): EnterTransition =
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(300)
    ) + fadeIn(
        animationSpec = tween(300)
    )

fun AnimatedContentTransitionScope<*>.detailPopExit(): ExitTransition =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(300)
    ) + fadeOut(
        animationSpec = tween(200)
    )


fun scaleFadeEnter(): EnterTransition =
    scaleIn(
        initialScale = 0.95f,
        animationSpec = tween(300)
    ) + fadeIn(
        animationSpec = tween(300)
    )

fun scaleFadeExit(): ExitTransition =
    scaleOut(
        targetScale = 0.95f,
        animationSpec = tween(250)
    ) + fadeOut(
        animationSpec = tween(200)
    )