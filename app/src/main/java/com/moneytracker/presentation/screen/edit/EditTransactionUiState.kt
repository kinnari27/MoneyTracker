package com.moneytracker.presentation.screen.edit

import com.moneytracker.domain.model.TransactionType

data class EditTransactionUiState(
    val id: Int = 0,
    val title: String = "",
    val amount: String = "",
    val selectedCategory: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val note: String = "",
    val isRecurring: Boolean = false,
    val isLoading: Boolean = true,   // loading from DB
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false,
    val error: String? = null,
    val notFound: Boolean = false
)