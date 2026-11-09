package com.reformistsinstitute.coredraft.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

data class GlassmorphicColors(
    val glassBackground: Color,
    val glassBorder: Color,
    val glassHighlight: Color,
    val searchBarBackground: Color,
    val chipBackground: Color,
    val subtleText: Color
)

val LocalGlassColors = staticCompositionLocalOf {
    GlassmorphicColors(
        glassBackground = DarkGlass,
        glassBorder = DarkBorder,
        glassHighlight = Color.White.copy(alpha = 0.15f),
        searchBarBackground = DarkSurfaceElevated,
        chipBackground = Color(0x33334155),
        subtleText = DarkTextSecondary
    )
}

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    secondary = PrimaryIndigo,
    onSecondary = Color.White,
    tertiary = AccentCyan,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    secondary = PrimaryIndigo,
    onSecondary = Color.White,
    tertiary = AccentCyan,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceElevated,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder
)

@Composable
fun CoreDraftTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val glassColors = if (darkTheme) {
        GlassmorphicColors(
            glassBackground = DarkGlass,
            glassBorder = DarkBorder,
            glassHighlight = Color.White.copy(alpha = 0.12f),
            searchBarBackground = DarkSurfaceElevated,
            chipBackground = Color(0x33334155),
            subtleText = DarkTextSecondary
        )
    } else {
        GlassmorphicColors(
            glassBackground = LightGlass,
            glassBorder = LightBorder,
            glassHighlight = Color.White.copy(alpha = 0.8f),
            searchBarBackground = LightSurfaceElevated,
            chipBackground = Color(0x1F0F172A),
            subtleText = LightTextSecondary
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalGlassColors provides glassColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = SquircleShapes,
            content = content
        )
    }
}
