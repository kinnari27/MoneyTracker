package com.moneytracker.presentation.screen.home

import com.moneytracker.domain.model.Transaction

data class HomeUiState(
    val isLoading: Boolean = true,
    val totalBalance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val budgetLimit: Double = 40000.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val error: String? = null
)
