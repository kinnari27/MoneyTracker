package com.moneytracker.domain.model

data class Budget(
    val id: Int = 0,
    val category: String,
    val limit: Double,
    val month: Int,   // 1-12
    val year: Int
) {
    // Spent amount is calculated separately from transactions
    val isOverBudget: Boolean get() = false // calculated in UseCase

    companion object {
        fun currentMonth(): Pair<Int, Int> {
            val calendar = java.util.Calendar.getInstance()
            return calendar.get(java.util.Calendar.MONTH) + 1 to
                    calendar.get(java.util.Calendar.YEAR)
        }
    }
}