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
    primary          = Indigo600,
    onPrimary        = White,
    primaryContainer = Indigo100,
    onPrimaryContainer = Indigo600,

    // Secondary (Slate mid)
    secondary          = Slate700,
    onSecondary        = White,
    secondaryContainer = Slate100,
    onSecondaryContainer = Slate700,

    // Tertiary (used for invest/amber tint)
    tertiary          = Invest600,
    onTertiary        = White,
    tertiaryContainer = Invest100,
    onTertiaryContainer = Invest600,

    // Error (Expense red)
    error          = Expense600,
    onError        = White,
    errorContainer = Expense100,
    onErrorContainer = Expense600,

    // Backgrounds & surfaces
    background    = Slate50,
    onBackground  = Slate900,

    surface           = White,
    onSurface         = Slate900,
    surfaceVariant    = Slate100,
    onSurfaceVariant  = Slate600,

    // Outline
    outline        = Slate200,
    outlineVariant = Slate100,

    // Inverse (for snackbars, etc.)
    inverseSurface    = Slate900,
    inverseOnSurface  = Slate50,
    inversePrimary    = Indigo400,

    scrim = Black
)

// ─────────────────────────────────────────────────────────────────────────────
//  Dark color scheme
// ─────────────────────────────────────────────────────────────────────────────
private val DarkColorScheme = darkColorScheme(

    // Primary (Indigo — lighter for dark)
    primary          = Indigo400,
    onPrimary        = Dark900,
    primaryContainer = Dark700,
    onPrimaryContainer = Indigo200,

    // Secondary
    secondary          = Slate400,
    onSecondary        = Dark900,
    secondaryContainer = Dark700,
    onSecondaryContainer = Slate200,

    // Tertiary
    tertiary          = Invest500,
    onTertiary        = Dark900,
    tertiaryContainer = Dark700,
    onTertiaryContainer = Invest100,

    // Error
    error          = Expense500,
    onError        = Dark900,
    errorContainer = Dark700,
    onErrorContainer = Expense100,

    // Backgrounds & surfaces
    background    = Dark900,
    onBackground  = Slate200,

    surface           = Dark800,
    onSurface         = Slate200,
    surfaceVariant    = Dark700,
    onSurfaceVariant  = Slate400,

    // Outline
    outline        = Dark600,
    outlineVariant = Dark700,

    // Inverse
    inverseSurface    = Slate100,
    inverseOnSurface  = Slate900,
    inversePrimary    = Indigo600,

    scrim = Black
)

// ─────────────────────────────────────────────────────────────────────────────
//  Extended colors — semantic tokens not in Material3 baseline
// ─────────────────────────────────────────────────────────────────────────────
data class ExtendedColors(
    // Header / screen top area
    val headerBackground: Color,
    val headerOnBackground: Color,
    val headerSubtle: Color,          // muted text in header

    // Transaction type colors
    val incomeColor: Color,
    val incomeContainer: Color,
    val expenseColor: Color,
    val expenseContainer: Color,
    val investColor: Color,
    val investContainer: Color,

    // Card surface slight tint
    val cardSurface: Color,
    val divider: Color,

    // Segmented control
    val segmentedBackground: Color,
    val segmentedSelected: Color,
    val segmentedOnSelected: Color,
)

val LightExtendedColors = ExtendedColors(
    headerBackground    = Slate900,
    headerOnBackground  = White,
    headerSubtle        = Slate400,

    incomeColor         = Income600,
    incomeContainer     = Income100,
    expenseColor        = Expense600,
    expenseContainer    = Expense100,
    investColor         = Invest600,
    investContainer     = Invest100,

    cardSurface         = White,
    divider             = Slate100,

    segmentedBackground = Slate100,
    segmentedSelected   = White,
    segmentedOnSelected = Slate900,
)

val DarkExtendedColors = ExtendedColors(
    headerBackground    = Dark900,
    headerOnBackground  = Slate100,
    headerSubtle        = Slate600,

    incomeColor         = Income500,
    incomeContainer     = Color(0xFF0D2E1A),
    expenseColor        = Expense500,
    expenseContainer    = Color(0xFF2D1010),
    investColor         = Invest500,
    investContainer     = Color(0xFF2D1F03),

    cardSurface         = Dark800,
    divider             = Dark600,

    segmentedBackground = Dark700,
    segmentedSelected   = Dark600,
    segmentedOnSelected = Slate100,
)

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

// ─────────────────────────────────────────────────────────────────────────────
//  Main theme composable
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun moneytrackerTheme(
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
        else      -> LightColorScheme
    }

    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    // Status bar color
    val view = LocalView.current
    if (!view.isInEditMode) {
        val statusBarColor = if (darkTheme) Dark900.toArgb() else Slate900.toArgb()
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = statusBarColor
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = Typography,
            shapes      = Shapes,
            content     = content
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Convenience accessor  — use  MaterialTheme.extended.incomeColor  anywhere
// ─────────────────────────────────────────────────────────────────────────────
val MaterialTheme.extended: ExtendedColors
    @Composable get() = LocalExtendedColors.current