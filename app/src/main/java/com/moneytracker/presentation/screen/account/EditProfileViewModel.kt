package com.moneytracker.presentation.screen.account

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
class EditProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    fun onNameChange(v: String) = _uiState.update { it.copy(name = v, error = null) }
    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v, error = null) }
    fun onPhoneChange(v: String) = _uiState.update { it.copy(phone = v, error = null) }
    fun onOccupationChange(v: String) = _uiState.update { it.copy(occupation = v) }
    fun onIncomeGoalChange(v: String) = _uiState.update { it.copy(incomeGoal = v) }

    fun save() {
        val s = _uiState.value
        if (s.name.isBlank()) {
            _uiState.update { it.copy(error = "Name cannot be empty") }; return
        }
        if (!s.email.contains("@")) {
            _uiState.update { it.copy(error = "Enter a valid email") }; return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(800) // TODO: replace with DataStore / API call
            _uiState.update { it.copy(isLoading = false, isSaved = true) }
        }
    }
}