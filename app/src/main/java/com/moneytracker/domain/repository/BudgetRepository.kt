package com.moneytracker.domain.repository

import com.moneytracker.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getBudgetsByMonth(month: Int, year: Int): Flow<List<Budget>>
    suspend fun getBudgetByCategoryAndMonth(category: String, month: Int, year: Int): Budget?
    suspend fun saveBudget(budget: Budget)
    suspend fun updateBudget(budget: Budget)
    suspend fun deleteBudget(budget: Budget)
}