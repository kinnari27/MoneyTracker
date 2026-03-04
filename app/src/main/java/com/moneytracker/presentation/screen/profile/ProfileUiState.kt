package com.moneytracker.presentation.screen.profile

import com.moneytracker.domain.model.User

data class ProfileUiState(
    val user: User? = null,
    val isPremium: Boolean = true,
    val totalTransactions: Int = 0,
    val totalCategories: Int = 0,
    val activeMonths: Int = 0,
    val isLoading: Boolean = true
)
