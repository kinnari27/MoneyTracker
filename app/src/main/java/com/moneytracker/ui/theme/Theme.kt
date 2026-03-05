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
    val warningContainer: Color,
    val inputBackground: Color,
    val isDark: Boolean,
    val warning: Color
)

private val LightAppColors = AppColors(
    primary = Primary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = PrimaryDark,
    background = Background,
    surface = Surface,
    surfaceElevated = Color(0xFFF7FAFD),
    surfaceVariant = SurfaceVariant,
    outline = Outline,
    outlineVariant = Color(0xFFDEEAF7),
    textPrimary = OnSurface,
    textSecondary = OnSurfaceVariant,
    success = Success,
    successContainer = Color(0xFFDCFCE7),
    error = Error,
    errorContainer = Color(0xFFFFE4E4),
    warning = Warning,
    warningContainer = Color(0xFFFFF3E0),
    inputBackground = Color(0xFFFFF3E0),
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
    successContainer = Color(0xFF14532D),
    error = ErrorDark,
    errorContainer = Color(0xFF7F1D1D),
    warning = WarningDark,
    warningContainer = Color(0xFF78350F),
    inputBackground = Color(0xFFFFF3E0),
    isDark = true
)

val LocalAppColors = staticCompositionLocalOf { LightAppColors }

private val M3Light = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = PrimaryDark,
    secondary = Secondary,
    onSecondary = OnPrimary,
    secondaryContainer = Color(0xFFCCE5FF),
    onSecondaryContainer = Color(0xFF00497A),
    tertiary = Accent,
    onTertiary = OnPrimary,
    tertiaryContainer = Color(0xFFCCF0FF),
    onTertiaryContainer = Color(0xFF004D66),
    background = Background,
    onBackground = OnSurface,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = Color(0xFFDEEAF7),
    error = Error,
    onError = OnPrimary,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF93000A),
    inverseSurface = OnSurface,
    inverseOnSurface = Surface,
    inversePrimary = PrimaryLight,
    scrim = Color(0xFF000000),
    surfaceTint = Primary,
)

private val M3Dark = darkColorScheme(
    primary = PrimaryDark_Mode,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = Color(0xFF38BDF8),
    onSecondary = Color(0xFF003548),
    secondaryContainer = Color(0xFF1A3A4A),
    onSecondaryContainer = Color(0xFFB3E5FC),
    tertiary = Accent,
    onTertiary = Color(0xFF003548),
    tertiaryContainer = Color(0xFF1A3A52),
    onTertiaryContainer = Color(0xFFB3E5FC),
    background = DarkBackground,
    onBackground = OnSurfaceDark,
    surface = DarkSurface,
    onSurface = OnSurfaceDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    error = ErrorDark,
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    inverseSurface = OnSurfaceDark,
    inverseOnSurface = DarkBackground,
    inversePrimary = Primary,
    scrim = Color(0xFF000000),
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