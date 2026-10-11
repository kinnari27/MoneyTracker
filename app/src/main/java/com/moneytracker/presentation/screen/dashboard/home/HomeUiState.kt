package com.moneytracker.presentation.screen.dashboard.home

import com.moneytracker.domain.model.Transaction

data class HomeUiState(
    val isLoading: Boolean = true,
    val userName: String = "",          // from DB
    val userInitial: String = "?",         // derived
    val profilePictureUrl: String? = null,
    val greeting: String = "Hello",     // time-based
    val totalBalance: Double = 0.0,         // income - expense - investment
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val totalInvested: Double = 0.0,
    val budgetLimit: Double = 0.0,         // from user.incomeGoal
    val recentTransactions: List<Transaction> = emptyList(),
    val error: String? = null
)
