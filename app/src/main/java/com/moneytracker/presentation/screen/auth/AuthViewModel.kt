package com.moneytracker.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.usecase.auth.LoginUserUseCase
import com.moneytracker.domain.usecase.auth.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUser: LoginUserUseCase,
    private val registerUser: RegisterUserUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // ── Step 1: Basic Info ────────────────────────────────────────────────────
    fun onNameChange(v: String) = _uiState.update { it.copy(name = v, error = null) }
    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v, error = null) }

    // ── Step 2: Personalization ───────────────────────────────────────────────
    fun onPhoneChange(v: String) = _uiState.update { it.copy(phone = v, error = null) }
    fun onOccupationChange(v: String) = _uiState.update { it.copy(occupation = v) }
    fun onIncomeGoalChange(v: String) = _uiState.update { it.copy(incomeGoal = v) }
    fun onBudgetLimitChange(v: String) = _uiState.update { it.copy(budgetLimit = v) }
    fun onCurrencyChange(v: String) = _uiState.update { it.copy(currency = v) }

    // ── Step 3: Security ──────────────────────────────────────────────────────
    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v, error = null) }
    fun onConfirmChange(v: String) = _uiState.update { it.copy(confirmPassword = v, error = null) }
    fun togglePasswordVisible() = _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    fun toggleConfirmVisible() = _uiState.update { it.copy(confirmVisible = !it.confirmVisible) }

    // ── Shared ────────────────────────────────────────────────────────────────
    fun resetSuccess() = _uiState.update { it.copy(isSuccess = false) }

    // ── Login ─────────────────────────────────────────────────────────────────
    fun login() {
        val s = _uiState.value
        // Client-side validation before network call
        if (s.email.isBlank()) {
            _uiState.update { it.copy(error = "Please enter your email") }; return
        }
        if (!s.email.contains("@")) {
            _uiState.update { it.copy(error = "Enter a valid email address") }; return
        }
        if (s.password.isBlank()) {
            _uiState.update { it.copy(error = "Please enter your password") }; return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            loginUser(s.email.trim(), s.password).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { e ->
                    // Repository returns specific messages:
                    // "User not found" → "No account found with this email"
                    // "Incorrect password" → "Incorrect password. Please try again."
                    val msg = when {
                        e.message?.contains("not found", ignoreCase = true) == true
                            -> "No account found with this email"

                        e.message?.contains("password", ignoreCase = true) == true
                            -> "Incorrect password. Please try again."

                        else -> e.message ?: "Login failed. Please try again."
                    }
                    _uiState.update { it.copy(isLoading = false, error = msg) }
                }
            )
        }
    }

    // ── Register ──────────────────────────────────────────────────────────────
    fun register() {
        val s = _uiState.value
        // Step-1 guards (should already be caught by UI, but double-check)
        if (s.name.isBlank()) {
            _uiState.update { it.copy(error = "Please enter your name") }; return
        }
        if (s.email.isBlank() || !s.email.contains("@")) {
            _uiState.update { it.copy(error = "Enter a valid email address") }; return
        }
        // Step-2 optional guards
        if (s.phone.isNotBlank() && s.phone.length != 10) {
            _uiState.update { it.copy(error = "Phone number must be 10 digits") }; return
        }
        // Step-3 guards
        if (s.password.length < 6) {
            _uiState.update { it.copy(error = "Password must be at least 6 characters") }; return
        }
        if (s.password != s.confirmPassword) {
            _uiState.update { it.copy(error = "Passwords don't match") }; return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            registerUser(
                name = s.name,
                email = s.email,
                password = s.password,
                phone = s.phone,
                occupation = s.occupation,
                incomeGoal = s.incomeGoal,
                budgetLimit = s.budgetLimit,
                currency = s.currency
            ).fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, isSuccess = true) } },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
            )
        }
    }

    // ── Forgot Password ───────────────────────────────────────────────────────
    fun sendResetLink() {
        val email = _uiState.value.email
        if (email.isBlank() || !email.contains("@")) {
            _uiState.update { it.copy(error = "Enter a valid email address") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            // Mocking a network delay for the reset link
            delay(1500)
            _uiState.update { it.copy(isLoading = false, isSuccess = true) }
        }
    }
}