package com.moneytracker.presentation.screen.home

import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.User

data class HomeUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val totalBalance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val budgetLimit: Double = 40000.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val error: String? = null
)
