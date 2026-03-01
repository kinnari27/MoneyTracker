package com.moneytracker.data.repository

import com.moneytracker.data.local.dao.TransactionDao
import com.moneytracker.data.mapper.toDomain
import com.moneytracker.data.mapper.toEntity
import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> =
        dao.getAllTransactions().map { list -> list.map { it.toDomain() } }

    override fun getTransactionsByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>> =
        dao.getTransactionsByDateRange(startDate, endDate)
            .map { list -> list.map { it.toDomain() } }

    override fun getTransactionsByType(type: String): Flow<List<Transaction>> =
        dao.getTransactionsByType(type).map { list -> list.map { it.toDomain() } }

    override fun getTotalIncome(startDate: Long, endDate: Long): Flow<Double?> =
        dao.getTotalIncome(startDate, endDate)

    override fun getTotalExpense(startDate: Long, endDate: Long): Flow<Double?> =
        dao.getTotalExpense(startDate, endDate)

    override suspend fun addTransaction(transaction: Transaction): Long =
        dao.insertTransaction(transaction.toEntity())

    override suspend fun updateTransaction(transaction: Transaction) =
        dao.updateTransaction(transaction.toEntity())

    override suspend fun deleteTransaction(transaction: Transaction) =
        dao.deleteTransaction(transaction.toEntity())

    override suspend fun deleteById(id: Int) =
        dao.deleteById(id)
}