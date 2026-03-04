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

// ─────────────────────────────────────────────────────────────────────────────
//  AppColors — semantic token bag for colors NOT in MaterialTheme
//  (Success, Warning, category tints, elevated surfaces, etc.)
//
//  Usage anywhere in a composable:
//      val c = AppTheme.colors
//      Text("hello", color = c.textPrimary)
//      Box(Modifier.background(c.success))
// ─────────────────────────────────────────────────────────────────────────────
data class AppColors(
    // Brand
    val primary             : Color,   // icon tint, FAB, active chip
    val primaryContainer    : Color,   // chip / tag background
    val onPrimaryContainer  : Color,   // text inside chip / tag

    // Surfaces
    val background          : Color,   // scaffold / page bg
    val surface             : Color,   // card, bottom-sheet bg
    val surfaceElevated     : Color,   // dialogs, overlaid cards
    val surfaceVariant      : Color,   // input fields, toggle track, unselected chip
    val outline             : Color,   // HorizontalDivider, border strokes
    val outlineVariant      : Color,   // subtle separator (thinner)

    // Text
    val textPrimary         : Color,   // headlines, amounts
    val textSecondary       : Color,   // labels, captions, hints

    // Semantic
    val success             : Color,
    val successContainer    : Color,   // tinted bg for success badge
    val error               : Color,
    val errorContainer      : Color,
    val warning             : Color,
    val warningContainer    : Color,

    // Meta
    val isDark              : Boolean
)

// ── Light token set ───────────────────────────────────────────────────────────
private val LightAppColors = AppColors(
    primary             = Primary,
    primaryContainer    = PrimaryContainer,
    onPrimaryContainer  = PrimaryDark,

    background          = Background,
    surface             = Surface,
    surfaceElevated     = Color(0xFFF7FAFD),
    surfaceVariant      = SurfaceVariant,
    outline             = Outline,
    outlineVariant      = Color(0xFFDEEAF7),

    textPrimary         = OnSurface,
    textSecondary       = OnSurfaceVariant,

    success             = Success,
    successContainer    = Color(0xFFDCFCE7),
    error               = Error,
    errorContainer      = Color(0xFFFFE4E4),
    warning             = Warning,
    warningContainer    = Color(0xFFFFF3E0),

    isDark              = false
)

// ── Dark token set ────────────────────────────────────────────────────────────
private val DarkAppColors = AppColors(
    primary             = PrimaryDark_Mode,
    primaryContainer    = PrimaryContainerDark,
    onPrimaryContainer  = OnPrimaryContainerDark,

    background          = DarkBackground,
    surface             = DarkSurface,
    surfaceElevated     = DarkSurfaceElevated,
    surfaceVariant      = DarkSurfaceVariant,
    outline             = DarkOutline,
    outlineVariant      = DarkOutlineVariant,

    textPrimary         = OnSurfaceDark,
    textSecondary       = OnSurfaceVariantDark,

    success             = SuccessDark,
    successContainer    = Color(0xFF14532D),   // dark green container
    error               = ErrorDark,
    errorContainer      = Color(0xFF7F1D1D),   // dark red container
    warning             = WarningDark,
    warningContainer    = Color(0xFF78350F),   // dark amber container

    isDark              = true
)

// ── CompositionLocal ──────────────────────────────────────────────────────────
val LocalAppColors = staticCompositionLocalOf { LightAppColors }

// ── Material 3 — Light Scheme ─────────────────────────────────────────────────
private val M3Light = lightColorScheme(
    primary                = Primary,
    onPrimary              = OnPrimary,
    primaryContainer       = PrimaryContainer,
    onPrimaryContainer     = PrimaryDark,
    secondary              = Secondary,
    onSecondary            = OnPrimary,
    secondaryContainer     = Color(0xFFCCE5FF),
    onSecondaryContainer   = Color(0xFF00497A),
    tertiary               = Accent,
    onTertiary             = OnPrimary,
    tertiaryContainer      = Color(0xFFCCF0FF),
    onTertiaryContainer    = Color(0xFF004D66),
    background             = Background,
    onBackground           = OnSurface,
    surface                = Surface,
    onSurface              = OnSurface,
    surfaceVariant         = SurfaceVariant,
    onSurfaceVariant       = OnSurfaceVariant,
    outline                = Outline,
    outlineVariant         = Color(0xFFDEEAF7),
    error                  = Error,
    onError                = OnPrimary,
    errorContainer         = Color(0xFFFFDAD6),
    onErrorContainer       = Color(0xFF93000A),
    inverseSurface         = OnSurface,
    inverseOnSurface       = Surface,
    inversePrimary         = PrimaryLight,
    scrim                  = Color(0xFF000000),
    surfaceTint            = Primary,
)

// ── Material 3 — Dark Scheme ──────────────────────────────────────────────────
private val M3Dark = darkColorScheme(
    // Primary
    primary                = PrimaryDark_Mode,          // #60A5FA — Blue-400
    onPrimary              = OnPrimaryDark,             // #003258 — dark navy
    primaryContainer       = PrimaryContainerDark,      // #1E3A5F — deep blue
    onPrimaryContainer     = OnPrimaryContainerDark,    // #BFDBFE

    // Secondary
    secondary              = Color(0xFF38BDF8),          // Sky-400
    onSecondary            = Color(0xFF003548),
    secondaryContainer     = Color(0xFF1A3A4A),
    onSecondaryContainer   = Color(0xFFB3E5FC),

    // Tertiary
    tertiary               = Accent,
    onTertiary             = Color(0xFF003548),
    tertiaryContainer      = Color(0xFF1A3A52),
    onTertiaryContainer    = Color(0xFFB3E5FC),

    // Background & Surface — layered from darkest to lightest
    background             = DarkBackground,            // #0F172A — deepest
    onBackground           = OnSurfaceDark,             // #F1F5F9
    surface                = DarkSurface,               // #1E293B — cards
    onSurface              = OnSurfaceDark,             // #F1F5F9
    surfaceVariant         = DarkSurfaceVariant,        // #334155 — inputs
    onSurfaceVariant       = OnSurfaceVariantDark,      // #94A3B8

    // Borders & dividers
    outline                = DarkOutline,               // #475569
    outlineVariant         = DarkOutlineVariant,        // #2D3F55

    // Error
    error                  = ErrorDark,                 // #F87171
    onError                = Color(0xFF690005),
    errorContainer         = Color(0xFF93000A),
    onErrorContainer       = Color(0xFFFFDAD6),

    // Inverse / scrim
    inverseSurface         = OnSurfaceDark,
    inverseOnSurface       = DarkBackground,
    inversePrimary         = Primary,
    scrim                  = Color(0xFF000000),
    surfaceTint            = PrimaryDark_Mode,
)

// ── Theme entry point ─────────────────────────────────────────────────────────
@Composable
fun MoneyTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val m3Scheme  = if (darkTheme) M3Dark        else M3Light
    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = m3Scheme,
            content     = content
        )
    }
}

// ── Convenience accessor ──────────────────────────────────────────────────────
object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current
}

// ── Alias so both `MoneyTrackerTheme` and `moneytrackerTheme` compile ─────────
// The uploaded MainActivity uses the lowercase-first variant; keep both working.
@Composable
fun moneytrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content  : @Composable () -> Unit
) = MoneyTrackerTheme(darkTheme = darkTheme, content = content)