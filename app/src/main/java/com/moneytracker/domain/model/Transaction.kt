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

// ── Transaction types ─────────────────────────────────────────────────────────
enum class TransactionType { INCOME, EXPENSE, INVESTMENT }

// ── Expense categories ────────────────────────────────────────────────────────
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

// ── Income categories ─────────────────────────────────────────────────────────
object IncomeCategory {
    const val SALARY = "Salary"
    const val FREELANCE = "Freelance"
    const val BUSINESS = "Business"
    const val RENTAL = "Rental"
    const val DIVIDEND = "Dividend"
    const val REFUND = "Refund"
    const val GIFT = "Gift"
    const val OTHER = "Other Income"

    val ALL = listOf(SALARY, FREELANCE, BUSINESS, RENTAL, DIVIDEND, REFUND, GIFT, OTHER)

    fun iconResFor(category: String): Int = when (category) {
        SALARY -> R.drawable.ic_salary
        FREELANCE -> R.drawable.ic_freelancer
        BUSINESS -> R.drawable.ic_shopping
        RENTAL -> R.drawable.ic_rental
        DIVIDEND -> R.drawable.ic_stat_largest
        REFUND -> R.drawable.ic_pills
        GIFT -> R.drawable.ic_star
        else -> R.drawable.ic_other
    }
}

// ── Investment categories ─────────────────────────────────────────────────────
object InvestmentCategory {
    const val FD = "Fixed Deposit"
    const val RD = "Recurring Deposit"
    const val LIC = "LIC / Insurance"
    const val MUTUAL_FUND = "Mutual Fund"
    const val STOCKS = "Stocks / SIP"
    const val PPF = "PPF / NPS"
    const val GOLD = "Gold"
    const val OTHER = "Other Investment"

    val ALL = listOf(FD, RD, LIC, MUTUAL_FUND, STOCKS, PPF, GOLD, OTHER)

    fun iconResFor(category: String): Int = when (category) {
        FD -> R.drawable.ic_stat_saved
        RD -> R.drawable.ic_stat_average
        LIC -> R.drawable.ic_locked
        MUTUAL_FUND -> R.drawable.ic_stat_largest
        STOCKS -> R.drawable.ic_export
        PPF -> R.drawable.ic_cloud
        GOLD -> R.drawable.ic_gold
        else -> R.drawable.ic_other
    }

    // Short display labels for the grid chips
    fun shortLabel(category: String): String = when (category) {
        FD -> "FD"
        RD -> "RD"
        LIC -> "LIC"
        MUTUAL_FUND -> "Mutual Fund"
        STOCKS -> "Stocks"
        PPF -> "PPF/NPS"
        GOLD -> "Gold"
        else -> "Other"
    }
}