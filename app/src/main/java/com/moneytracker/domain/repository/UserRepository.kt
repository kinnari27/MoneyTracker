package com.moneytracker.domain.repository

import com.moneytracker.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val observeCurrentUser: Flow<User?>
    suspend fun updateBiometric(userId: Int, enabled: Boolean)
    suspend fun insertUser(user: User)
}