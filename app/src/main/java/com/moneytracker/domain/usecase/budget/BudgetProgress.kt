package com.moneytracker.domain.usecase.budget

import com.moneytracker.domain.model.Budget

data class BudgetProgress(
    val budget: Budget,
    val spent: Double,
    val remaining: Double = budget.limit - spent,
    val percentage: Float = if (budget.limit > 0) (spent / budget.limit).toFloat() else 0f,
    val isOverBudget: Boolean = spent > budget.limit
)
