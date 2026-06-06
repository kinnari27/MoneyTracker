package com.moneytracker.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ── Shape scale — clean, subtle corners, no bubble effects ───────────────────
val Shapes = Shapes(

    // Chips, badges, tags, small buttons
    extraSmall = RoundedCornerShape(6.dp),

    // Input fields, list items, small cards
    small = RoundedCornerShape(10.dp),

    // Standard cards, dialogs, bottom sheets
    medium = RoundedCornerShape(14.dp),

    // Large cards, modals
    large = RoundedCornerShape(18.dp),

    // Full-screen bottom sheets
    extraLarge = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
)

// ── Named aliases for use in composables ────────────────────────────────────
val ChipShape = RoundedCornerShape(6.dp)
val InputShape = RoundedCornerShape(10.dp)
val CardShape = RoundedCornerShape(14.dp)
val LargeCardShape = RoundedCornerShape(18.dp)
val ButtonShape = RoundedCornerShape(12.dp)
val BadgeShape = RoundedCornerShape(4.dp)

// Pill shape — only for toggle/segmented control, NOT for general cards
val PillShape = RoundedCornerShape(50)