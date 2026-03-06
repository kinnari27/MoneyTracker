package com.moneytracker.domain.usecase.auth

import com.moneytracker.domain.model.User
import com.moneytracker.domain.repository.UserRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveCurrentUserUseCase @Inject constructor(
    private val repo: UserRepository
) {
    operator fun invoke(): Flow<User?> = repo.observeCurrentUser
}