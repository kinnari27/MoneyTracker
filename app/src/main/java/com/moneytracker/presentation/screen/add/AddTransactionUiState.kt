package com.moneytracker.presentation.screen.add

import com.moneytracker.domain.model.TransactionType

data class AddTransactionUiState(
    val title: String = "",
    val amount: String = "",
    val selectedCategory: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val note: String = "",
    val isRecurring: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)