package com.moneytracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val primary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val surfaceVariant: Color,
    val outline: Color,
    val outlineVariant: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val success: Color,
    val successContainer: Color,
    val error: Color,
    val errorContainer: Color,
    val warning: Color,
    val warningContainer: Color,
    val inputBackground: Color,
    val isDark: Boolean
)

private val LightAppColors = AppColors(
    primary = Primary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    background = Background,
    surface = Surface,
    surfaceElevated = SurfaceElevated,
    surfaceVariant = SurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    textPrimary = OnSurface,
    textSecondary = OnSurfaceVariant,
    success = Success,
    successContainer = SuccessContainer,
    error = Error,
    errorContainer = ErrorContainer,
    warning = Warning,
    warningContainer = WarningContainer,
    inputBackground = InputBackground,
    isDark = false
)

private val DarkAppColors = AppColors(
    primary = PrimaryDark_Mode,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceElevated = DarkSurfaceElevated,
    surfaceVariant = DarkSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    textPrimary = OnSurfaceDark,
    textSecondary = OnSurfaceVariantDark,
    success = SuccessDark,
    successContainer = SuccessContainerDark,
    error = ErrorDark,
    errorContainer = ErrorContainerDark,
    warning = WarningDark,
    warningContainer = WarningContainerDark,
    inputBackground = InputBackgroundDark,
    isDark = true
)

val LocalAppColors = staticCompositionLocalOf { LightAppColors }

private val M3Light = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnPrimary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Accent,
    onTertiary = OnPrimary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    background = Background,
    onBackground = OnSurface,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    error = Error,
    onError = OnPrimary,
    errorContainer = ErrorContainerM3,
    onErrorContainer = OnErrorContainer,
    inverseSurface = OnSurface,
    inverseOnSurface = Surface,
    inversePrimary = PrimaryLight,
    scrim = Scrim,
    surfaceTint = Primary,
)

private val M3Dark = darkColorScheme(
    primary = PrimaryDark_Mode,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = Accent,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    background = DarkBackground,
    onBackground = OnSurfaceDark,
    surface = DarkSurface,
    onSurface = OnSurfaceDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    inverseSurface = OnSurfaceDark,
    inverseOnSurface = DarkBackground,
    inversePrimary = Primary,
    scrim = Scrim,
    surfaceTint = PrimaryDark_Mode,
)

@Composable
fun MoneyTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val m3Scheme = if (darkTheme) M3Dark else M3Light
    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = m3Scheme,
            content = content
        )
    }
}

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current
}

@Composable
fun moneytrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) = MoneyTrackerTheme(darkTheme = darkTheme, content = content)