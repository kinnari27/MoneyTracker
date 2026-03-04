package com.moneytracker.data.repository

import com.moneytracker.data.local.dao.UserDao
import com.moneytracker.data.mapper.toDomain
import com.moneytracker.data.mapper.toEntity
import com.moneytracker.domain.model.User
import com.moneytracker.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override val observeCurrentUser: Flow<User?> = userDao.observeUser().map { it?.toDomain() }

    override suspend fun updateBiometric(userId: Int, enabled: Boolean) {
        userDao.updateBiometric(userId, enabled)
    }

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user.toEntity())
    }

    override suspend fun signOut() {
        userDao.deleteUser()
    }
}
