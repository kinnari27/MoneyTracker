package com.moneytracker.presentation.screen.settings.changePin

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
class ChangePinViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePinUiState())
    val uiState: StateFlow<ChangePinUiState> = _uiState.asStateFlow()

    // Hardcoded for demo — replace with DataStore/encrypted prefs
    private val storedPin = "1234"

    fun onDigit(digit: String) {
        val s = _uiState.value
        _uiState.update { it.copy(error = null) }

        when (s.step) {
            PinStep.CURRENT -> {
                val next = (s.currentPin + digit).take(4)
                _uiState.update { it.copy(currentPin = next) }
                if (next.length == 4) validateCurrentPin(next)
            }

            PinStep.NEW -> {
                val next = (s.newPin + digit).take(4)
                _uiState.update { it.copy(newPin = next) }
                if (next.length == 4) _uiState.update { it.copy(step = PinStep.CONFIRM) }
            }

            PinStep.CONFIRM -> {
                val next = (s.confirmPin + digit).take(4)
                _uiState.update { it.copy(confirmPin = next) }
                if (next.length == 4) validateConfirmPin(s.newPin, next)
            }
        }
    }

    fun onDelete() {
        val s = _uiState.value
        _uiState.update { it.copy(error = null) }
        when (s.step) {
            PinStep.CURRENT -> _uiState.update { it.copy(currentPin = it.currentPin.dropLast(1)) }
            PinStep.NEW -> _uiState.update { it.copy(newPin = it.newPin.dropLast(1)) }
            PinStep.CONFIRM -> _uiState.update { it.copy(confirmPin = it.confirmPin.dropLast(1)) }
        }
    }

    private fun validateCurrentPin(pin: String) {
        if (pin == storedPin) {
            _uiState.update { it.copy(step = PinStep.NEW, currentPin = "") }
        } else {
            viewModelScope.launch {
                _uiState.update { it.copy(error = "Incorrect PIN. Try again.") }
                delay(600)
                _uiState.update { it.copy(currentPin = "", error = null) }
            }
        }
    }

    private fun validateConfirmPin(newPin: String, confirm: String) {
        if (newPin == confirm) {
            viewModelScope.launch {
                // TODO: Save new PIN to DataStore
                _uiState.update { it.copy(isComplete = true) }
                delay(1200)
                _uiState.update { it.copy(isComplete = true) } // triggers nav back
            }
        } else {
            viewModelScope.launch {
                _uiState.update { it.copy(error = "PINs don't match. Try again.") }
                delay(600)
                _uiState.update {
                    it.copy(
                        confirmPin = "",
                        newPin = "",
                        step = PinStep.NEW,
                        error = null
                    )
                }
            }
        }
    }
}