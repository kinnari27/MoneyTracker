package com.moneytracker.presentation.screen.dashboard.history

import com.moneytracker.domain.model.Transaction

data class HistoryUiState(
    val isLoading: Boolean = true,
    val allTransactions: List<Transaction> = emptyList(),
    val filteredTransactions: List<Transaction> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val totalInvestment: Double = 0.0,
    val filter: TransactionFilter = TransactionFilter.ALL,
    val searchQuery: String = "",
    val transactionToDelete: Transaction? = null,
    val error: String? = null
)

enum class TransactionFilter { ALL, INCOME, EXPENSE, INVESTMENT }