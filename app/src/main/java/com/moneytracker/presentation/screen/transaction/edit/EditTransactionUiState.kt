package com.moneytracker.presentation.screen.transaction.edit

import com.moneytracker.domain.model.TransactionType
import java.util.Date

data class EditTransactionUiState(
    val id: Int = 0,
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
    val notFound: Boolean = false,

    // FD specific fields
    val maturityDate: Date = Date(System.currentTimeMillis() + 31536000000L),
    val interestRate: String = "",
    val maturityAmount: Double = 0.0,

    // RD specific fields
    val rdMonthlyAmount: String = "",
    val rdMaturityDate: Date = Date(System.currentTimeMillis() + 31536000000L),
    val rdInterestRate: String = "",
    val rdMaturityAmount: Double = 0.0
)