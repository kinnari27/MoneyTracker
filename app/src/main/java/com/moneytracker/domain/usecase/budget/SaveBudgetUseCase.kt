package com.moneytracker.domain.usecase.budget

import com.moneytracker.domain.model.Budget
import com.moneytracker.domain.repository.BudgetRepository
import javax.inject.Inject

class SaveBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget): Result<Unit> {
        return try {
            require(budget.limit > 0) { "Budget limit must be greater than 0" }
            require(budget.category.isNotBlank()) { "Category cannot be empty" }
            val existing = repository.getBudgetByCategoryAndMonth(
                budget.category, budget.month, budget.year
            )
            if (existing != null) {
                repository.updateBudget(budget.copy(id = existing.id))
            } else {
                repository.saveBudget(budget)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
