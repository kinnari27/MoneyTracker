package com.moneytracker.presentation.screen.history

import com.moneytracker.domain.model.Transaction

enum class TransactionFilter { ALL, INCOME, EXPENSE }

data class HistoryUiState(
    val isLoading: Boolean = true,
    val allTransactions: List<Transaction> = emptyList(),
    val filteredTransactions: List<Transaction> = emptyList(),
    val filter: TransactionFilter = TransactionFilter.ALL,
    val searchQuery: String = "",
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val error: String? = null
)