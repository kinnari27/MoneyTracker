package com.moneytracker.presentation.screen.settings.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun onCurrentPasswordChange(v: String) = _uiState.update { it.copy(currentPassword = v, error = null) }
    fun onNewPasswordChange(v: String) = _uiState.update { it.copy(newPassword = v, error = null) }
    fun onConfirmPasswordChange(v: String) = _uiState.update { it.copy(confirmPassword = v, error = null) }
    
    fun togglePasswordVisibility() = _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    fun toggleConfirmVisibility() = _uiState.update { it.copy(confirmVisible = !it.confirmVisible) }

    fun next() {
        val s = _uiState.value
        when (s.step) {
            PasswordStep.CURRENT -> verifyCurrentPassword()
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

    private fun verifyCurrentPassword() {
        val s = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val user = userRepository.getCurrentUser()
            if (user == null) {
                _uiState.update { it.copy(isLoading = false, error = "Session error. Please login again.") }
                return@launch
            }

            userRepository.loginUser(user.email, s.currentPassword).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, step = PasswordStep.NEW, error = null) }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false, error = "Incorrect current password") }
                }
            )
        }
    }

    private fun submit() {
        val s = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val user = userRepository.getCurrentUser()
            if (user == null) {
                _uiState.update { it.copy(isLoading = false, error = "Session error") }
                return@launch
            }

            userRepository.updatePin(user.id, s.newPassword).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isComplete = true) }
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Failed to update password") }
                }
            )
        }
    }
}
