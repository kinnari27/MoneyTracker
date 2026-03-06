package com.moneytracker.presentation.screen.settings.profile

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val userPhone: String = "",
    val currency: String = "INR",
    val isPremium: Boolean = false,
    val totalTransactions: Int = 0,
    val totalCategories: Int = 0,
    val activeMonths: Int = 0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val totalInvested: Double = 0.0,
    val isLoading: Boolean = true
)
