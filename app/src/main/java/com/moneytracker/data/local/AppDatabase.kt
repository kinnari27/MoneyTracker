package com.moneytracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moneytracker.data.local.dao.BudgetDao
import com.moneytracker.data.local.dao.TransactionDao
import com.moneytracker.data.local.entity.BudgetEntity
import com.moneytracker.data.local.entity.TransactionEntity

@Database(
    entities  = [TransactionEntity::class, BudgetEntity::class],
    version   = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
}