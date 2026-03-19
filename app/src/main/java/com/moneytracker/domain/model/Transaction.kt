package com.moneytracker.domain.model

import com.moneytracker.R
import java.util.Date

data class Transaction(
    val id: Int = 0,
    val amount: Double,
    val category: String,
    val type: TransactionType,
    val note: String = "",
    val date: Date = Date(),
    val isRecurring: Boolean = false,
    // FD specific fields
    val maturityDate: Date? = null,
    val interestRate: Double? = null,
    val maturityAmount: Double? = null,
    // RD specific fields
    val rdMonthlyAmount: Double? = null,
    val rdTenureMonths: Int? = null,
    val rdMaturityDate: Date? = null,
    val rdInterestRate: Double? = null,
    val rdMaturityAmount: Double? = null
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
    const val REFUND = "Refund"

    val ALL = listOf(SALARY, FREELANCE, BUSINESS, RENTAL, REFUND)

    fun iconResFor(category: String): Int = when (category) {
        SALARY -> R.drawable.ic_salary
        FREELANCE -> R.drawable.ic_freelancer
        BUSINESS -> R.drawable.ic_shopping
        RENTAL -> R.drawable.ic_rental
        REFUND -> R.drawable.ic_refund
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

    val ALL = listOf(FD, RD, LIC, MUTUAL_FUND, STOCKS, PPF, GOLD)

    fun iconResFor(category: String): Int = when (category) {
        FD -> R.drawable.ic_fd
        RD -> R.drawable.ic_rd
        LIC -> R.drawable.ic_lic
        MUTUAL_FUND -> R.drawable.ic_mutual_fund
        STOCKS -> R.drawable.ic_stock
        PPF -> R.drawable.ic_ppf
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