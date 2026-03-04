package com.moneytracker.di

import com.moneytracker.data.repository.BudgetRepositoryImpl
import com.moneytracker.data.repository.TransactionRepositoryImpl
import com.moneytracker.data.repository.UserRepositoryImpl
import com.moneytracker.domain.repository.BudgetRepository
import com.moneytracker.domain.repository.TransactionRepository
import com.moneytracker.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        impl: BudgetRepositoryImpl
    ): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}