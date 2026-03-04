package com.moneytracker.presentation.screen.splash

// ── Navigation destinations after splash ─────────────────────────────────────
sealed class SplashDestination {
    object Loading : SplashDestination()
    object Login : SplashDestination()
    object Register : SplashDestination()
    object Home : SplashDestination()
}

// ── UiState ───────────────────────────────────────────────────────────────────
data class SplashUiState(
    val destination: SplashDestination = SplashDestination.Loading,
    val isReady: Boolean = false
)