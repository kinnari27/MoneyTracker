package com.moneytracker.presentation.screen.settings.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    // Hardcoded for demo — replace with DataStore/encrypted prefs or repository call
    private val storedPassword = "1234"

    fun onCurrentPasswordChange(v: String) = _uiState.update { it.copy(currentPassword = v, error = null) }
    fun onNewPasswordChange(v: String) = _uiState.update { it.copy(newPassword = v, error = null) }
    fun onConfirmPasswordChange(v: String) = _uiState.update { it.copy(confirmPassword = v, error = null) }
    
    fun togglePasswordVisibility() = _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    fun toggleConfirmVisibility() = _uiState.update { it.copy(confirmVisible = !it.confirmVisible) }

    fun next() {
        val s = _uiState.value
        when (s.step) {
            PasswordStep.CURRENT -> {
                if (s.currentPassword == storedPassword) {
                    _uiState.update { it.copy(step = PasswordStep.NEW, error = null) }
                } else {
                    _uiState.update { it.copy(error = "Incorrect current password") }
                }
            }
            PasswordStep.NEW -> {
                if (s.newPassword.length < 4) {
                    _uiState.update { it.copy(error = "Password must be at least 4 characters") }
                } else {
                    _uiState.update { it.copy(step = PasswordStep.CONFIRM, error = null) }
                }
            }
            PasswordStep.CONFIRM -> {
                if (s.newPassword == s.confirmPassword) {
                    submit()
                } else {
                    _uiState.update { it.copy(error = "Passwords do not match") }
                }
            }
        }
    }

    private fun submit() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            // Simulate API call
            delay(1000)
            _uiState.update { it.copy(isLoading = false, isComplete = true) }
        }
    }
}
