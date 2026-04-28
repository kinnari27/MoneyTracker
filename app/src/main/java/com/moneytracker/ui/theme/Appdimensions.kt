package com.moneytracker.ui.theme

import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
//  Spacing scale
// ─────────────────────────────────────────────────────────────────────────────
object Spacing {
    val xxs  = 2.dp
    val xs   = 4.dp
    val sm   = 8.dp
    val md   = 12.dp
    val lg   = 16.dp
    val xl   = 20.dp
    val xxl  = 24.dp
    val xxxl = 32.dp

    // Screen-level horizontal padding
    val screenPadding = 16.dp

    // Card internal padding
    val cardPadding = 16.dp

    // Gap between list items
    val itemGap = 12.dp
}

// ─────────────────────────────────────────────────────────────────────────────
//  Elevation (keep flat — very low elevations only)
// ─────────────────────────────────────────────────────────────────────────────
object Elevation {
    val none   = 0.dp
    val card   = 0.dp   // flat cards, border only
    val dialog = 2.dp
    val navbar = 0.dp   // bottom nav: flat with top border
}

// ─────────────────────────────────────────────────────────────────────────────
//  Component sizing
// ─────────────────────────────────────────────────────────────────────────────
object Size {
    // Buttons
    val buttonHeight      = 52.dp
    val buttonHeightSmall = 40.dp

    // Input fields
    val inputHeight = 52.dp

    // Bottom navigation
    val bottomNavHeight = 64.dp

    // Icon sizes
    val iconSm = 16.dp
    val iconMd = 20.dp
    val iconLg = 24.dp

    // Avatar / initials circle
    val avatarSm = 32.dp
    val avatarMd = 44.dp
    val avatarLg = 64.dp

    // Category chip
    val chipHeight = 36.dp

    // Transaction icon container
    val txIconSize = 40.dp
    val txIconRadius = 12.dp

    // Header min height
    val headerMinHeight = 200.dp

    // Stroke / divider
    val dividerThickness = 0.5.dp
    val borderWidth      = 0.5.dp
}