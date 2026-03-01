package com.moneytracker.domain.usecase.budget

import com.moneytracker.domain.model.Budget
import com.moneytracker.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBudgetsByMonthUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    operator fun invoke(month: Int, year: Int): Flow<List<Budget>> =
        repository.getBudgetsByMonth(month, year)
}
