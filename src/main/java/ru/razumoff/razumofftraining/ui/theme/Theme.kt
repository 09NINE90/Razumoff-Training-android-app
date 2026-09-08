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

// ============================================================
// Темная тема
// ============================================================

private val DarkColorScheme = darkColorScheme(

    // Основной брендовый акцент
    primary = RedPrimary,
    onPrimary = Color.White,

    // Красный контейнер для selected / active состояний
    primaryContainer = RedPrimaryDark,
    onPrimaryContainer = RedPrimaryVeryLight,

    // Вторичный цвет — НЕ красный
    secondary = GreyLight,
    onSecondary = TextDark,

    secondaryContainer = SurfaceVariantDark,
    onSecondaryContainer = TextLight,

    // Третичный цвет — нейтральный
    tertiary = RedPrimaryLight,
    onTertiary = Color.Black,

    // Основной фон
    background = BackgroundDark,
    onBackground = TextLight,

    // Основная поверхность
    surface = SurfaceDark,
    onSurface = TextLight,

    // Поверхность второго уровня
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = GreyLight,

    // Ошибка — отдельный semantic color
    error = ErrorDark,
    onError = Color.White,

    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,

    // Нейтральные границы
    outline = GreyMedium,
    outlineVariant = GreyDark
)

// ============================================================
// Светлая тема
// ============================================================

private val LightColorScheme = lightColorScheme(

    // Основной брендовый акцент
    primary = RedPrimary,
    onPrimary = Color.White,

    // Светлый красный контейнер
    primaryContainer = RedPrimarySuperLight,
    onPrimaryContainer = RedPrimaryDark,

    // Вторичный цвет — нейтральный
    secondary = GreyDark,
    onSecondary = Color.White,

    secondaryContainer = SurfaceVariantLight,
    onSecondaryContainer = TextDark,

    // Третичный цвет
    tertiary = RedPrimaryDark,
    onTertiary = Color.White,

    // Фон
    background = BackgroundLight,
    onBackground = TextDark,

    // Поверхность
    surface = SurfaceLight,
    onSurface = TextDark,

    // Поверхность второго уровня
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = GreyMedium,

    // Ошибка
    error = ErrorLight,
    onError = Color.White,

    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,

    // Нейтральные границы
    outline = GreyMedium,
    outlineVariant = GreyLight
)

@Composable
fun RazumoffTrainingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController =
                WindowCompat.getInsetsController(window, view)

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