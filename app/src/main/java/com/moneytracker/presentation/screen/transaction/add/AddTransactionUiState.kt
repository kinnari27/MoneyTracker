package com.moneytracker.presentation.screen.transaction.add

import com.moneytracker.domain.model.TransactionType
import java.util.Date

data class AddTransactionUiState(
    val id: Int? = null,
    val title: String = "",
    val amount: String = "",
    val selectedCategory: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val note: String = "",
    val date: Date = Date(),
    val isRecurring: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
) {
    val isEditMode: Boolean get() = id != null
}