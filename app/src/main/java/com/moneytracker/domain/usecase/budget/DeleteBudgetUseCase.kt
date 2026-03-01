package com.moneytracker.domain.usecase.budget

import com.moneytracker.domain.model.Budget
import com.moneytracker.domain.repository.BudgetRepository
import javax.inject.Inject

class DeleteBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget) =
        repository.deleteBudget(budget)
}
