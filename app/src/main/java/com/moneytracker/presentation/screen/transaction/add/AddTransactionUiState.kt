package com.moneytracker.presentation.screen.transaction.add

import com.moneytracker.domain.model.TransactionType
import java.util.Date

data class AddTransactionUiState(
    val id: Int? = null,
    val amount: String = "",
    val selectedCategory: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val note: String = "",
    val date: Date = Date(),
    val isRecurring: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null,

    // FD specific fields
    val maturityDate: Date = Date(System.currentTimeMillis() + 31536000000L), // Default 1 year later
    val interestRate: String = "",
    val maturityAmount: Double = 0.0,

    // RD specific fields
    val rdMonthlyAmount: String = "",
    val rdMaturityDate: Date = Date(System.currentTimeMillis() + 31536000000L), // Default 1 year later
    val rdInterestRate: String = "",
    val rdMaturityAmount: Double = 0.0
) {
    val isEditMode: Boolean get() = id != null
}