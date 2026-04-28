package com.moneytracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── Font family (uses system Inter / default sans-serif) ──────────────────────
// Swap FontFamily.Default for a custom font if you add one to res/font/
val AppFontFamily = FontFamily.Default

val Typography = Typography(

    // Large balance display  e.g. "₹1,24,500"
    displayLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize    = 36.sp,
        lineHeight  = 44.sp,
        letterSpacing = (-0.5).sp
    ),

    // Section balance subtitles
    displayMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize    = 28.sp,
        lineHeight  = 36.sp,
        letterSpacing = (-0.3).sp
    ),

    displaySmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize    = 22.sp,
        lineHeight  = 28.sp
    ),

    // Screen headings  e.g. "Add Transaction"
    headlineLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize    = 20.sp,
        lineHeight  = 28.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize    = 18.sp,
        lineHeight  = 24.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize    = 16.sp,
        lineHeight  = 22.sp
    ),

    // Card titles, form labels
    titleLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize    = 15.sp,
        lineHeight  = 20.sp
    ),

    titleMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize    = 14.sp,
        lineHeight  = 20.sp,
        letterSpacing = 0.1.sp
    ),

    titleSmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize    = 13.sp,
        lineHeight  = 18.sp,
        letterSpacing = 0.1.sp
    ),

    // Body content
    bodyLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize    = 15.sp,
        lineHeight  = 22.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize    = 13.sp,
        lineHeight  = 19.sp
    ),

    bodySmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize    = 12.sp,
        lineHeight  = 17.sp
    ),

    // Chips, badges, tab labels
    labelLarge = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize    = 13.sp,
        lineHeight  = 18.sp,
        letterSpacing = 0.1.sp
    ),

    labelMedium = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize    = 11.sp,
        lineHeight  = 16.sp,
        letterSpacing = 0.5.sp
    ),

    // Section headers  e.g. "SPENDING ALERTS"
    labelSmall = TextStyle(
        fontFamily = AppFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize    = 10.sp,
        lineHeight  = 14.sp,
        letterSpacing = 0.8.sp
    )
)