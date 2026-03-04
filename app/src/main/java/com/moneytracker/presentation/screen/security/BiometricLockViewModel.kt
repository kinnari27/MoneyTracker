package com.moneytracker.presentation.screen.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
            userRepository.observeCurrentUser()
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