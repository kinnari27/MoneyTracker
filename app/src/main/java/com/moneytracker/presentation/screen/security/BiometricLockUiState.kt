package com.moneytracker.presentation.screen.security

data class BiometricLockUiState(
    val biometricEnabled: Boolean = false,
    val faceIdEnabled: Boolean = false,
    // 0=Immediately, 1=1 min, 2=5 min, 3=15 min
    val autoLockOption: Int = 0,
    val isLoading: Boolean = true,
    val isSaved: Boolean = false,
    val userId: Int = 0
)