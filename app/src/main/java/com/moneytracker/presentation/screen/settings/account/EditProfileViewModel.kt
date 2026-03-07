package com.moneytracker.presentation.screen.settings.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.repository.UserRepository
import com.moneytracker.domain.usecase.auth.UpdateProfileUseCase
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
    private val userRepository: UserRepository,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        loadFromDb()
    }

    // ── Load current user from Room ───────────────────────────────────────────
    private fun loadFromDb() {
        viewModelScope.launch {
            userRepository.observeCurrentUser
                .filterNotNull()
                .take(1)                        // one-shot seed for form fields
                .collect { user ->
                    _uiState.update {
                        it.copy(
                            userId = user.id,
                            name = user.name,
                            email = user.email,
                            phone = user.phone,
                            occupation = user.occupation,
                            incomeGoal = user.incomeGoal,
                            budgetLimit = user.budgetLimit,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onNameChange(v: String) = _uiState.update { it.copy(name = v, error = null) }
    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v, error = null) }
    fun onPhoneChange(v: String) = _uiState.update { it.copy(phone = v) }
    fun onOccupationChange(v: String) = _uiState.update { it.copy(occupation = v) }
    fun onIncomeGoalChange(v: Double) = _uiState.update { it.copy(incomeGoal = v) }
    fun onBudgetLimitChange(v: Double) = _uiState.update { it.copy(budgetLimit = v) }

    // ── Save to Room ──────────────────────────────────────────────────────────
    fun save() {
        val s = _uiState.value
        if (s.name.isBlank()) {
            _uiState.update { it.copy(error = "Name cannot be empty") }; return
        }
        if (s.email.isBlank()) {
            _uiState.update { it.copy(error = "Email cannot be empty") }; return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            updateProfileUseCase(
                id = s.userId,
                name = s.name.trim(),
                email = s.email.trim(),
                phone = s.phone.trim(),
                occupation = s.occupation.trim(),
                incomeGoal = s.incomeGoal,
                budgetLimit = s.budgetLimit
            )
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
