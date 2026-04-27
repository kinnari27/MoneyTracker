package com.moneytracker.presentation.screen.settings.changePassword

enum class PasswordStep { CURRENT, NEW, CONFIRM }

data class ChangePasswordUiState(
    val step: PasswordStep = PasswordStep.CURRENT,
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isComplete: Boolean = false
)