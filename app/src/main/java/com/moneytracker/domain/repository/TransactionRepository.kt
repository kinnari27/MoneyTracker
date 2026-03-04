package com.moneytracker.domain.repository

import com.moneytracker.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>>
    fun getTransactionsByType(type: String): Flow<List<Transaction>>
    fun getTotalIncome(startDate: Long, endDate: Long): Flow<Double?>
    fun getTotalExpense(startDate: Long, endDate: Long): Flow<Double?>
    suspend fun addTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    suspend fun deleteById(id: Int)
    suspend fun getTransactionById(id: Int): Transaction?
}