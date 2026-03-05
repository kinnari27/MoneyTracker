package com.moneytracker.presentation.screen.settings.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BiometricLockViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BiometricLockUiState())
    val uiState: StateFlow<BiometricLockUiState> = _uiState.asStateFlow()

    init {
        loadFromDb()
    }

    // ── Load current biometric setting from Room ───────────────────────────────
    private fun loadFromDb() {
        viewModelScope.launch {
            userRepository.observeCurrentUser
                .filterNotNull()
                .take(1)
                .collect { user ->
                    _uiState.update {
                        it.copy(
                            biometricEnabled = user.biometricEnabled,
                            userId = user.id,
                            isLoading = false
                        )
                    }
                }
        }
    }

    // ── Toggle handlers ───────────────────────────────────────────────────────
    fun onBiometricToggle() {
        val newValue = !_uiState.value.biometricEnabled
        _uiState.update { it.copy(biometricEnabled = newValue) }
        saveBiometric(newValue)
    }

    fun onFaceIdToggle() {
        _uiState.update { it.copy(faceIdEnabled = !it.faceIdEnabled) }
        // Face ID uses the same biometric flag in Room
        saveBiometric(_uiState.value.biometricEnabled || _uiState.value.faceIdEnabled)
    }

    fun onAutoLockSelect(idx: Int) = _uiState.update { it.copy(autoLockOption = idx) }

    // ── Persist to Room ───────────────────────────────────────────────────────
    private fun saveBiometric(enabled: Boolean) {
        val userId = _uiState.value.userId
        if (userId == 0) return
        viewModelScope.launch {
            userRepository.updateBiometric(userId, enabled)
            _uiState.update { it.copy(isSaved = true) }
        }
    }
}