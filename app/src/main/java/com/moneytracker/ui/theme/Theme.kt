package com.moneytracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ─────────────────────────────────────────────────────────────────────────────
//  Light color scheme  — Indigo + Slate
// ─────────────────────────────────────────────────────────────────────────────
private val LightColorScheme = lightColorScheme(

    // Primary (Indigo)
    primary = Indigo600,
    onPrimary = White,
    primaryContainer = Indigo100,
    onPrimaryContainer = Indigo600,

    // Secondary (Slate mid)
    secondary = Slate700,
    onSecondary = White,
    secondaryContainer = Slate100,
    onSecondaryContainer = Slate700,

    // Tertiary (used for invest/amber tint)
    tertiary = Invest600,
    onTertiary = White,
    tertiaryContainer = Invest100,
    onTertiaryContainer = Invest600,

    // Error (Expense red)
    error = Expense600,
    onError = White,
    errorContainer = Expense100,
    onErrorContainer = Expense600,

    // Backgrounds & surfaces
    background = Slate50,
    onBackground = Slate900,

    surface = White,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate600,

    // Outline
    outline = Slate200,
    outlineVariant = Slate100,

    // Inverse (for snackbars, etc.)
    inverseSurface = Slate900,
    inverseOnSurface = Slate50,
    inversePrimary = Indigo400,

    scrim = Black
)

// ─────────────────────────────────────────────────────────────────────────────
//  Dark color scheme
// ─────────────────────────────────────────────────────────────────────────────
private val DarkColorScheme = darkColorScheme(

    // Primary (Indigo — lighter for dark)
    primary = Indigo400,
    onPrimary = Dark900,
    primaryContainer = Dark700,
    onPrimaryContainer = Indigo200,

    // Secondary
    secondary = Slate400,
    onSecondary = Dark900,
    secondaryContainer = Dark700,
    onSecondaryContainer = Slate200,

    // Tertiary
    tertiary = Invest500,
    onTertiary = Dark900,
    tertiaryContainer = Dark700,
    onTertiaryContainer = Invest100,

    // Error
    error = Expense500,
    onError = Dark900,
    errorContainer = Dark700,
    onErrorContainer = Expense100,

    // Backgrounds & surfaces
    background = Dark900,
    onBackground = Slate200,

    surface = Dark800,
    onSurface = Slate200,
    surfaceVariant = Dark700,
    onSurfaceVariant = Slate400,

    // Outline
    outline = Dark600,
    outlineVariant = Dark700,

    // Inverse
    inverseSurface = Slate100,
    inverseOnSurface = Slate900,
    inversePrimary = Indigo600,

    scrim = Black
)

// ─────────────────────────────────────────────────────────────────────────────
//  Extended colors — semantic tokens not in Material3 baseline
// ─────────────────────────────────────────────────────────────────────────────
data class ExtendedColors(
    val isDark: Boolean,
    // Header / screen top area
    val headerBackground: Color,
    val headerOnBackground: Color,
    val headerSubtle: Color,          // muted text in header

    // Brand colors
    val primary: Color,
    val accent: Color,

    // Transaction type colors
    val incomeColor: Color,
    val incomeContainer: Color,
    val expenseColor: Color,
    val expenseContainer: Color,
    val investColor: Color,
    val investContainer: Color,

    // Semantic colors
    val textSecondary: Color,
    val success: Color,
    val error: Color,
    val errorContainer: Color,
    val warning: Color,

    // Card surface slight tint
    val cardSurface: Color,
    val divider: Color,

    // Segmented control
    val segmentedBackground: Color,
    val segmentedSelected: Color,
    val segmentedOnSelected: Color,

    // Inputs
    val inputBackground: Color,
    val outline: Color,
)

val LightExtendedColors = ExtendedColors(
    isDark = false,
    headerBackground = Slate900,
    headerOnBackground = White,
    headerSubtle = Slate400,

    primary = Indigo600,
    accent = Indigo400,

    incomeColor = Income600,
    incomeContainer = Income100,
    expenseColor = Expense600,
    expenseContainer = Expense100,
    investColor = Invest600,
    investContainer = Invest100,

    textSecondary = Slate600,
    success = Income600,
    error = Expense600,
    errorContainer = Expense100,
    warning = Invest500,

    cardSurface = White,
    divider = Slate100,

    segmentedBackground = Slate100,
    segmentedSelected = White,
    segmentedOnSelected = Slate900,

    inputBackground = Slate100,
    outline = Slate200,
)

val DarkExtendedColors = ExtendedColors(
    isDark = true,
    headerBackground = Dark900,
    headerOnBackground = Slate100,
    headerSubtle = Slate600,

    primary = Indigo400,
    accent = Indigo200,

    incomeColor = Income500,
    incomeContainer = Color(0xFF0D2E1A),
    expenseColor = Expense500,
    expenseContainer = Color(0xFF2D1010),
    investColor = Invest500,
    investContainer = Color(0xFF2D1F03),

    textSecondary = Slate400,
    success = Income500,
    error = Expense500,
    errorContainer = Color(0xFF2D1010),
    warning = Invest500,

    cardSurface = Dark800,
    divider = Dark600,

    segmentedBackground = Dark700,
    segmentedSelected = Dark600,
    segmentedOnSelected = Slate100,

    inputBackground = Dark700,
    outline = Dark600,
)

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

object AppTheme {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}

// ─────────────────────────────────────────────────────────────────────────────
//  Main theme composable
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun MoneyTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is disabled — we want our custom palette always
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    // Status bar icon color: all app screen headers are dark (Indigo / Slate900 / Dark900),
    // so status bar icons must remain light (white) for clear contrast and readability.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Convenience accessor  — use  MaterialTheme.extended.incomeColor  anywhere
// ─────────────────────────────────────────────────────────────────────────────
val MaterialTheme.extended: ExtendedColors
    @Composable get() = LocalExtendedColors.current

// ── Compatibility aliases for legacy top-level access ────────────────────────
val Success: Color @Composable get() = AppTheme.colors.success
val Warning: Color @Composable get() = AppTheme.colors.warning
val Error: Color @Composable get() = AppTheme.colors.error
val Primary: Color @Composable get() = AppTheme.colors.primary
val Accent: Color @Composable get() = AppTheme.colors.accent
val PrimaryDark: Color @Composable get() = AppTheme.colors.primary
val OnSurface: Color @Composable get() = MaterialTheme.colorScheme.onSurface
val OnSurfaceVariant: Color @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
val Surface: Color @Composable get() = MaterialTheme.colorScheme.surface
val SurfaceVariant: Color @Composable get() = MaterialTheme.colorScheme.surfaceVariant

@Composable
fun moneytrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) = MoneyTrackerTheme(darkTheme, dynamicColor, content)
