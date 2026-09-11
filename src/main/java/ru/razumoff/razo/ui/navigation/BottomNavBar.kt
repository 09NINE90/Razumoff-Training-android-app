package ru.razumoff.razo.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.razumoff.razo.R

@Composable
fun BottomNavBar(
    navController: NavController,
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

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route



    Surface(
        modifier = modifier
            .height(70.dp),
        shape = RoundedCornerShape(50.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 12.dp,
        tonalElevation = 0.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize(),
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = item.title,
                            modifier = Modifier.size(34.dp)
                        )
                    },
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        if (currentRoute != item.screen.route) {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    )
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