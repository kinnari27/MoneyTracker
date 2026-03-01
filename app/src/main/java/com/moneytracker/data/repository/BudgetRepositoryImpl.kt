package com.moneytracker.data.repository

import com.moneytracker.data.local.dao.BudgetDao
import com.moneytracker.data.mapper.toDomain
import com.moneytracker.data.mapper.toEntity
import com.moneytracker.domain.model.Budget
import com.moneytracker.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val dao: BudgetDao
) : BudgetRepository {

    override fun getBudgetsByMonth(month: Int, year: Int): Flow<List<Budget>> =
        dao.getBudgetsByMonth(month, year).map { list -> list.map { it.toDomain() } }

    override suspend fun getBudgetByCategoryAndMonth(
        category: String,
        month: Int,
        year: Int
    ): Budget? = dao.getBudgetByCategoryAndMonth(category, month, year)?.toDomain()

    override suspend fun saveBudget(budget: Budget) =
        dao.insertBudget(budget.toEntity())

    override suspend fun updateBudget(budget: Budget) =
        dao.updateBudget(budget.toEntity())

    override suspend fun deleteBudget(budget: Budget) =
        dao.deleteBudget(budget.toEntity())
}