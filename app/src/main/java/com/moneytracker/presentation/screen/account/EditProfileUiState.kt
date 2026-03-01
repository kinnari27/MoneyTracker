package com.moneytracker.presentation.screen.account

data class EditProfileUiState(
    val name        : String  = "Rahul Sharma",
    val email       : String  = "rahul.sharma@gmail.com",
    val phone       : String  = "+91 98765 43210",
    val occupation  : String  = "Software Engineer",
    val incomeGoal  : String  = "80000",
    val isLoading   : Boolean = false,
    val isSaved     : Boolean = false,
    val error       : String? = null
)