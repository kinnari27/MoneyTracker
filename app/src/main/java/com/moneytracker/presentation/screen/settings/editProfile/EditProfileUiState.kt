package com.moneytracker.presentation.screen.settings.editProfile

data class EditProfileUiState(
    val userId: Int = 0,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val occupation: String = "",
    val incomeGoal: Double = 0.0,
    val budgetLimit: Double = 0.0,
    val profilePictureUrl: String? = null,
    val isGoogleConnected: Boolean = false,
    val isSyncingGoogle: Boolean = false,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)
