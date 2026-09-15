package ru.razumoff.razo.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AnimatedBottomNavBar(
    navController: NavHostController,
    visible: Boolean = true,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(300)
        ) + fadeIn(
            animationSpec = tween(200)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(250)
        ) + fadeOut(
            animationSpec = tween(150)
        ),
        modifier = modifier
    ) {
        BottomNavBar(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 25.dp
                )
        )
    }
}