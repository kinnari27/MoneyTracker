package com.moneytracker.presentation.screen.settings.security

data class BiometricLockUiState(
    val biometricEnabled: Boolean = false,
    val faceIdEnabled: Boolean = false,
    val autoLockOption: Int = 0,
    val isLoading: Boolean = true,
    val isSaved: Boolean = false,
    val userId: Int = 0
)