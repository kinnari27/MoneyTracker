package com.moneytracker.presentation.screen.settings.biometricLock

data class BiometricLockUiState(
    val biometricEnabled: Boolean = false,
    val faceIdEnabled: Boolean = false,
    val autoLockOption: Int = 0   // index into ["Immediately","1 min","5 min","15 min"]
)