package com.moneytracker.presentation.screen.account

data class EditProfileUiState(
    val name        : String  = "",
    val email       : String  = "",
    val phone       : String  = "",
    val occupation  : String  = "",
    val incomeGoal  : String  = "",
    val budgetLimit : String  = "",
    val currency    : String  = "INR",
    val isLoading   : Boolean = false,
    val isSaved     : Boolean = false,
    val error       : String? = null
)
