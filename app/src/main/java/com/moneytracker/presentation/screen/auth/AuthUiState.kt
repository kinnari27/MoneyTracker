package com.moneytracker.presentation.screen.auth

data class AuthUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val passwordVisible: Boolean = false,
    val confirmVisible: Boolean = false
)