package com.moneytracker.presentation.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.usecase.auth.LoginUserUseCase
import com.moneytracker.domain.usecase.auth.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    // ── Field updates ─────────────────────────────────────────────────────────
    fun onNameChange(v: String) = _uiState.update { it.copy(name = v, error = null) }
    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v, error = null) }
    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v, error = null) }
    fun onConfirmChange(v: String) = _uiState.update { it.copy(confirmPassword = v, error = null) }
    fun onPhoneChange(v: String) = _uiState.update { it.copy(phone = v, error = null) }
    fun onOccupationChange(v: String) = _uiState.update { it.copy(occupation = v) }
    fun onIncomeGoalChange(v: String) = _uiState.update { it.copy(incomeGoal = v) }
    fun togglePasswordVisible() = _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    fun toggleConfirmVisible() = _uiState.update { it.copy(confirmVisible = !it.confirmVisible) }
    fun clearError() = _uiState.update { it.copy(error = null) }
    fun resetSuccess() = _uiState.update { it.copy(isSuccess = false) }

    // ── Login ─────────────────────────────────────────────────────────────────
    fun login() {
        val s = _uiState.value
        if (s.email.isBlank()) {
            _uiState.update { it.copy(error = "Please enter your email") }; return
        }
        if (s.password.isBlank()) {
            _uiState.update { it.copy(error = "Please enter your password") }; return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            loginUser(s.email.trim(), s.password).fold(
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

    // ── Register ──────────────────────────────────────────────────────────────
    fun register() {
        val s = _uiState.value
        if (s.name.isBlank()) {
            _uiState.update { it.copy(error = "Please enter your name") }; return
        }
        if (s.email.isBlank() || !s.email.contains("@")) {
            _uiState.update { it.copy(error = "Enter a valid email address") }; return
        }
        if (s.phone.length != 10) {
            _uiState.update { it.copy(error = "Phone number must be exactly 10 digits") }; return
        }
        if (s.password.length < 6) {
            _uiState.update { it.copy(error = "Password must be at least 6 characters") }; return
        }
        if (s.password != s.confirmPassword) {
            _uiState.update { it.copy(error = "Passwords don't match") }; return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            registerUser(
                s.name.trim(),
                s.email.trim(),
                s.password,
                s.phone.trim(),
                s.occupation.trim(),
                s.incomeGoal.trim()
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
}
