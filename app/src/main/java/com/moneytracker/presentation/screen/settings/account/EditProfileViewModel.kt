package com.moneytracker.presentation.screen.settings.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.model.User
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
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private var currentUserId: Int = 0
    private var currentBiometric: Boolean = false

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            userRepository.observeCurrentUser
                .filterNotNull()
                .take(1)
                .collect { user ->
                    currentUserId = user.id
                    currentBiometric = user.biometricEnabled
                    _uiState.update {
                        it.copy(
                            name = user.name,
                            email = user.email,
                            phone = user.phone,
                            occupation = user.occupation,
                            incomeGoal = user.incomeGoal,
                            budgetLimit = user.budgetLimit,
                            currency = user.currency
                        )
                    }
                }
        }
    }

    fun onNameChange(v: String) = _uiState.update { it.copy(name = v, error = null) }
    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v, error = null) }
    fun onPhoneChange(v: String) = _uiState.update { it.copy(phone = v, error = null) }
    fun onOccupationChange(v: String) = _uiState.update { it.copy(occupation = v) }
    fun onIncomeGoalChange(v: String) = _uiState.update { it.copy(incomeGoal = v) }
    fun onBudgetLimitChange(v: String) = _uiState.update { it.copy(budgetLimit = v) }
    fun onCurrencyChange(v: String) = _uiState.update { it.copy(currency = v) }

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
            val updatedUser = User(
                id = currentUserId,
                name = s.name,
                email = s.email,
                phone = s.phone,
                occupation = s.occupation,
                incomeGoal = s.incomeGoal,
                budgetLimit = s.budgetLimit,
                currency = s.currency,
                biometricEnabled = currentBiometric
            )
            userRepository.insertUser(updatedUser)
            _uiState.update { it.copy(isLoading = false, isSaved = true) }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepository.signOut()
        }
    }
}
