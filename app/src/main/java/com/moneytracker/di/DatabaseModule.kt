package com.moneytracker.di

import android.content.Context
import androidx.room.Room
import com.moneytracker.data.local.AppDatabase
import com.moneytracker.data.local.dao.BudgetDao
import com.moneytracker.data.local.dao.TransactionDao
import com.moneytracker.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "expense_tracker.db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao =
        db.transactionDao()

    @Provides
    fun provideBudgetDao(db: AppDatabase): BudgetDao =
        db.budgetDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao =
        db.userDao()
}