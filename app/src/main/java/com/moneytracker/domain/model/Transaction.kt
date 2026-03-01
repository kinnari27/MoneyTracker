package com.moneytracker.domain.model

import com.moneytracker.R
import java.util.Date

data class Transaction(
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val type: TransactionType,
    val note: String = "",
    val date: Date = Date(),
    val isRecurring: Boolean = false
)

enum class TransactionType { INCOME, EXPENSE }

// Category constants used across the app
object Category {
    const val FOOD = "Food"
    const val TRANSPORT = "Transport"
    const val SHOPPING = "Shopping"
    const val HEALTH = "Health"
    const val ENTERTAINMENT = "Entertainment"
    const val BILLS = "Bills"
    const val EDUCATION = "Education"
    const val OTHER = "Other"

    val ALL = listOf(FOOD, TRANSPORT, SHOPPING, HEALTH, ENTERTAINMENT, BILLS, EDUCATION, OTHER)

    fun iconResFor(category: String): Int = when (category) {
        FOOD -> R.drawable.ic_food
        TRANSPORT -> R.drawable.ic_transport
        SHOPPING -> R.drawable.ic_shopping
        HEALTH -> R.drawable.ic_pills
        ENTERTAINMENT -> R.drawable.ic_entertainment
        BILLS -> R.drawable.ic_bill
        EDUCATION -> R.drawable.ic_book
        else -> R.drawable.ic_other
    }
}