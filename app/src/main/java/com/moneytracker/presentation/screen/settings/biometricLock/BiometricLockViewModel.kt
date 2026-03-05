package com.moneytracker.presentation.screen.settings.biometricLock

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BiometricLockViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BiometricLockUiState())
    val uiState: StateFlow<BiometricLockUiState> = _uiState.asStateFlow()

    fun onBiometricToggle() = _uiState.update { it.copy(biometricEnabled = !it.biometricEnabled) }
    fun onFaceIdToggle() = _uiState.update { it.copy(faceIdEnabled = !it.faceIdEnabled) }
    fun onAutoLockSelect(idx: Int) = _uiState.update { it.copy(autoLockOption = idx) }
}