package com.moneytracker.domain.usecase.trasaction

import com.moneytracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMonthlySummaryUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    fun getIncome(startDate: Long, endDate: Long): Flow<Double?> =
        repository.getTotalIncome(startDate, endDate)

    fun getExpense(startDate: Long, endDate: Long): Flow<Double?> =
        repository.getTotalExpense(startDate, endDate)
}