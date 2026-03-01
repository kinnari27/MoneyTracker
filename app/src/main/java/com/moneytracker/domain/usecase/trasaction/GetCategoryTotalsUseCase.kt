package com.moneytracker.domain.usecase.trasaction

import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.TransactionType
import javax.inject.Inject

class GetCategoryTotalsUseCase @Inject constructor() {
    operator fun invoke(transactions: List<Transaction>): Map<String, Double> =
        transactions
            .filter { it.type == TransactionType.EXPENSE }
            .groupBy { it.category }
            .mapValues { (_, list) -> list.sumOf { it.amount } }
            .toList()
            .sortedByDescending { it.second }
            .toMap()
}