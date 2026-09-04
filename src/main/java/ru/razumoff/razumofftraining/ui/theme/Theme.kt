package ru.razumoff.razumofftraining.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Темная тема
private val DarkColorScheme = darkColorScheme(
    primary = RedPrimary,
    onPrimary = Color.White,
    primaryContainer = RedPrimaryDark,
    onPrimaryContainer = RedPrimaryVeryLight,
    secondary = RedPrimaryLight,
    onSecondary = Color.Black,
    secondaryContainer = RedPrimaryDark,
    onSecondaryContainer = RedPrimaryVeryLight,
    tertiary = RedPrimary,
    onTertiary = Color.White,
    background = BackgroundDark,
    onBackground = TextLight,
    surface = SurfaceDark,
    onSurface = TextLight,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = GreyLight,
    error = RedPrimary,
    onError = Color.White,
    errorContainer = RedPrimaryDark,
    onErrorContainer = RedPrimaryVeryLight,
    outline = RedPrimary,
    outlineVariant = RedPrimaryDark
)

// Светлая тема
private val LightColorScheme = lightColorScheme(
    primary = RedPrimary,
    onPrimary = Color.White,
    primaryContainer = RedPrimarySuperLight,
    onPrimaryContainer = RedPrimaryDark,
    secondary = RedPrimary,
    onSecondary = Color.White,
    secondaryContainer = RedPrimarySuperLight,
    onSecondaryContainer = RedPrimaryDark,
    tertiary = RedPrimaryLight,
    onTertiary = Color.Black,
    background = BackgroundLight,
    onBackground = TextDark,
    surface = SurfaceLight,
    onSurface = TextDark,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = GreyMedium,
    error = ErrorLight,
    onError = Color.White,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    outline = RedPrimary,
    outlineVariant = RedPrimarySuperLight
)

@Composable
fun RazumoffTrainingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}