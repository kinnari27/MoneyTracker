package com.moneytracker.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary          = Primary,
    onPrimary        = OnPrimary,
    primaryContainer = PrimaryContainer,
    secondary        = Secondary,
    background       = Background,
    surface          = Surface,
    onSurface        = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
    outline          = Outline,
    error            = Error,
)

private val DarkColorScheme = darkColorScheme(
    primary          = PrimaryLight,
    onPrimary        = OnPrimary,
    primaryContainer = PrimaryDark,
    secondary        = Accent,
    background       = OnSurface,
    surface          = OnSurface,
    onSurface        = Surface,
    onSurfaceVariant = SurfaceVariant,
    outline          = Outline,
    error            = Error,
)

@Composable
fun moneytrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            // Since the app bar is blue (dark), we want light icons (isAppearanceLightStatusBars = false)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content     = content
    )
}
