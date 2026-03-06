package com.moneytracker.presentation.screen.settings.account

data class EditProfileUiState(
    val userId: Int = 0,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val occupation: String = "",
    val incomeGoal: String = "",
    val isLoading: Boolean = true,   // true while loading from DB
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)
