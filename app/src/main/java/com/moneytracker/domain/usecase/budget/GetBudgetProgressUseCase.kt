package com.moneytracker.domain.usecase.budget

import com.moneytracker.domain.model.Budget
import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.TransactionType
import java.util.Calendar
import javax.inject.Inject

class GetBudgetProgressUseCase @Inject constructor() {
    operator fun invoke(
        budgets: List<Budget>,
        transactions: List<Transaction>,
        month: Int,
        year: Int
    ): List<BudgetProgress> {
        return budgets.map { budget ->
            val spent = transactions
                .filter { t ->
                    t.type == TransactionType.EXPENSE &&
                            t.category == budget.category &&
                            isInMonth(t.date.time, month, year)
                }
                .sumOf { it.amount }
            BudgetProgress(budget = budget, spent = spent)
        }
    }

    private fun isInMonth(timestamp: Long, month: Int, year: Int): Boolean {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp
        return cal.get(Calendar.MONTH) + 1 == month &&
                cal.get(Calendar.YEAR) == year
    }
}
