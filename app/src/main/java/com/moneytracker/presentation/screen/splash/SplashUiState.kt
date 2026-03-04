package com.moneytracker.presentation.screen.splash

// ── Navigation destinations after splash ─────────────────────────────────────
sealed class SplashDestination {
    object Loading : SplashDestination()   // still deciding
    object Login : SplashDestination()   // account exists → go to login
    object Register : SplashDestination()   // first launch   → go to register
    object Home : SplashDestination()   // logged-in user → skip auth
}

// ── UiState ───────────────────────────────────────────────────────────────────
data class SplashUiState(
    val destination: SplashDestination = SplashDestination.Loading,
    val isReady: Boolean = false   // true once delay + check done
)