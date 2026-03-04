package com.moneytracker.ui.theme

import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────────────────────
//  LIGHT MODE
// ─────────────────────────────────────────────────────────────────────────────

// Primary Brand — Ocean Blue
val Primary          = Color(0xFF1565C0)
val PrimaryLight     = Color(0xFF42A5F5)
val PrimaryDark      = Color(0xFF0D47A1)
val PrimaryContainer = Color(0xFFDBEAFE)   // chip / container bg in light
val Secondary        = Color(0xFF0288D1)
val Accent           = Color(0xFF00B0FF)

// Surfaces & Backgrounds
val Background       = Color(0xFFF0F4F8)   // page bg
val Surface          = Color(0xFFFFFFFF)   // card bg
val SurfaceVariant   = Color(0xFFE8EFF7)   // input / chip fill
val Outline          = Color(0xFFC5D8EE)   // dividers, borders

// Text
val OnSurface        = Color(0xFF1A2332)   // primary text
val OnSurfaceVariant = Color(0xFF4A6080)   // secondary text / hint
val OnPrimary        = Color(0xFFFFFFFF)   // text on primary buttons

// Semantic — Light
val Success          = Color(0xFF43A047)
val Error            = Color(0xFFEF5350)
val Warning          = Color(0xFFFB8C00)

// ─────────────────────────────────────────────────────────────────────────────
//  DARK MODE
// ─────────────────────────────────────────────────────────────────────────────

val DarkBackground        = Color(0xFF0F172A)   // Slate-950
val DarkSurface           = Color(0xFF1E293B)   // Slate-800
val DarkSurfaceElevated   = Color(0xFF243044)   // between 800-700
val DarkSurfaceVariant    = Color(0xFF334155)   // Slate-700 — inputs, chips
val DarkOutline           = Color(0xFF475569)   // Slate-600 — dividers
val DarkOutlineVariant    = Color(0xFF2D3F55)   // subtle separator

// Text — Dark
val OnSurfaceDark         = Color(0xFFF1F5F9)   // Slate-100 — primary text
val OnSurfaceVariantDark  = Color(0xFF94A3B8)   // Slate-400 — secondary / hint

// Primary — Dark (must be lighter to clear WCAG AA on dark bg)
val PrimaryDark_Mode      = Color(0xFF60A5FA)   // Blue-400
val PrimaryContainerDark  = Color(0xFF1E3A5F)   // deep navy chip/container
val OnPrimaryDark         = Color(0xFF003258)   // text ON the bright blue button
val OnPrimaryContainerDark = Color(0xFFBFDBFE)  // text inside primary container

// Semantic — Dark (brighter so they pop on dark surfaces)
val SuccessDark           = Color(0xFF4ADE80)   // Green-400
val ErrorDark             = Color(0xFFF87171)   // Red-400
val WarningDark           = Color(0xFFFBBF24)   // Amber-400

// ─────────────────────────────────────────────────────────────────────────────
//  CATEGORY COLORS (vivid — legible in both modes)
// ─────────────────────────────────────────────────────────────────────────────
val CategoryFood          = Color(0xFFFF7043)   // Deep Orange 400
val CategoryTransport     = Color(0xFF42A5F5)   // Blue 400
val CategoryShopping      = Color(0xFFAB47BC)   // Purple 400
val CategoryHealth        = Color(0xFF26A69A)   // Teal 400
val CategoryEntertainment = Color(0xFFFFA726)   // Orange 400
val CategoryBills         = Color(0xFF78909C)   // Blue Grey 400
val CategoryEducation     = Color(0xFF5C6BC0)   // Indigo 400
val CategoryOther         = Color(0xFF8D6E63)   // Brown 400