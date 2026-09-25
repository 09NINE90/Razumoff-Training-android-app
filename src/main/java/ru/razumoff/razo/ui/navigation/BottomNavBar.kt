package ru.razumoff.razo.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.razumoff.razo.R
import ru.razumoff.razo.navigateToRoot

@Composable
fun BottomNavBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem(
            title = stringResource(R.string.steps),
            icon = ImageVector.vectorResource(R.drawable.pedometer_step_counter),
            screen = Screen.Steps
        ),
        BottomNavItem(
            title = stringResource(R.string.workouts),
            icon = ImageVector.vectorResource(R.drawable.my_workout_plan),
            screen = Screen.Workout
        ),
        BottomNavItem(
            title = stringResource(R.string.profile),
            icon = ImageVector.vectorResource(R.drawable.ic_user),
            screen = Screen.UserProfile
        )
    )

    val currentRoute =
        navController.currentBackStackEntryAsState()
            .value
            ?.destination
            ?.route

    Surface(
        modifier = modifier.height(70.dp),
        shape = RoundedCornerShape(50.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->

                val selected = currentRoute == item.screen.route

                BottomNavItem(
                    item = item,
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            navigateToRoot(
                                navController = navController,
                                route = item.screen.route
                            )
                        }
                    }
                )
            }
        }
    }
}

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val screen: Screen
)

@Composable
private fun BottomNavItem(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        } else {
            Color.Transparent
        },
        animationSpec = tween(
            durationMillis = 250
        ),
        label = "background"
    )

    val iconColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        },
        animationSpec = tween(
            durationMillis = 250
        ),
        label = "iconColor"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (selected) 1.1f else 1f,
        animationSpec = tween(
            durationMillis = 250
        ),
        label = "iconScale"
    )

    Surface(
        onClick = onClick,
        color = backgroundColor,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 8.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = iconColor,
                modifier = Modifier
                    .size(28.dp)
                    .scale(iconScale)
            )

            AnimatedVisibility(
                visible = selected
            ) {
                Row {
                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = item.title,
                        color = iconColor
                    )
                }
            }
        }
    }
}