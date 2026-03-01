package com.moneytracker.presentation.screen.profile

data class ProfileUiState(
    val userName: String = "Rahul Sharma",
    val userEmail: String = "rahul.sharma@gmail.com",
    val isPremium: Boolean = true,
    val totalTransactions: Int = 0,
    val totalCategories: Int = 0,
    val activeMonths: Int = 0,
    val isLoading: Boolean = true
)