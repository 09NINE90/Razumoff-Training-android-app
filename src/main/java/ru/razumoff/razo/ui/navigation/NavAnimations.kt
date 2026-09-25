package ru.razumoff.razo.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry


/**
 * Экраны нижнего/root уровня.
 *
 * Переходы между ними:
 *
 * Steps <-> Workout <-> UserProfile
 *
 * выполняются горизонтально.
 */
val horizontalRoutes = listOf(
    Screen.Steps.route,
    Screen.Workout.route,
    Screen.UserProfile.route
)


private fun isHorizontalRoute(route: String?): Boolean {
    return route in horizontalRoutes
}

private fun horizontalIndex(route: String?): Int {
    return horizontalRoutes.indexOf(route)
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.isForwardHorizontalNavigation(): Boolean {
    val from = horizontalIndex(initialState.destination.route)
    val to = horizontalIndex(targetState.destination.route)

    return from != -1 && to != -1 && to > from
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.isBackwardHorizontalNavigation(): Boolean {
    val from = horizontalIndex(initialState.destination.route)
    val to = horizontalIndex(targetState.destination.route)

    return from != -1 && to != -1 && to < from
}


/**
 * Определяет, является ли текущий переход
 * переходом между двумя root-экранами.
 *
 * Например:
 *
 * Workout -> UserProfile = true
 * Steps -> Workout         = true
 *
 * Workout -> Exercises     = false
 * Exercises -> Workout     = false
 * Templates -> Detail      = false
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.isHorizontalNavigation(): Boolean {
    val from = initialState.destination.route
    val to = targetState.destination.route

    return isHorizontalRoute(from) && isHorizontalRoute(to)
}


private const val NAV_ANIMATION_DURATION = 300


/* ============================================================
 * HORIZONTAL
 * ============================================================ */

/**
 * Push:
 *
 * A -> B
 *
 * Новый экран появляется справа и движется влево.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.horizontalEnter(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(NAV_ANIMATION_DURATION)
    ) + fadeIn(
        animationSpec = tween(NAV_ANIMATION_DURATION)
    )
}


/**
 * Push:
 *
 * A -> B
 *
 * Старый экран уходит влево.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.horizontalExit(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(NAV_ANIMATION_DURATION)
    ) + fadeOut(
        animationSpec = tween(200)
    )
}


/**
 * Pop:
 *
 * B -> A
 *
 * Предыдущий экран появляется слева
 * и движется вправо.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.horizontalPopEnter(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(NAV_ANIMATION_DURATION)
    ) + fadeIn(
        animationSpec = tween(NAV_ANIMATION_DURATION)
    )
}


/**
 * Pop:
 *
 * B -> A
 *
 * Текущий экран уходит вправо.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.horizontalPopExit(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(NAV_ANIMATION_DURATION)
    ) + fadeOut(
        animationSpec = tween(200)
    )
}


/* ============================================================
 * VERTICAL
 * ============================================================ */

/**
 * Push:
 *
 * A -> B
 *
 * Новый detail-экран появляется снизу
 * и движется вверх.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.verticalEnter(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(NAV_ANIMATION_DURATION)
    ) + fadeIn(
        animationSpec = tween(NAV_ANIMATION_DURATION)
    )
}


/**
 * Push:
 *
 * A -> B
 *
 * Старый экран уходит вверх.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.verticalExit(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween(NAV_ANIMATION_DURATION)
    ) + fadeOut(
        animationSpec = tween(200)
    )
}


/**
 * Pop:
 *
 * B -> A
 *
 * Предыдущий экран появляется сверху
 * и движется вниз.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.verticalPopEnter(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(NAV_ANIMATION_DURATION)
    ) + fadeIn(
        animationSpec = tween(NAV_ANIMATION_DURATION)
    )
}


/**
 * Pop:
 *
 * B -> A
 *
 * Текущий detail-экран уходит вниз.
 */
fun AnimatedContentTransitionScope<NavBackStackEntry>.verticalPopExit(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween(NAV_ANIMATION_DURATION)
    ) + fadeOut(
        animationSpec = tween(200)
    )
}
