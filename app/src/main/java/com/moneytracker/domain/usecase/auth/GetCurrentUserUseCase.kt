package com.moneytracker.domain.usecase.auth

import com.moneytracker.domain.model.User
import com.moneytracker.domain.repository.UserRepository
import jakarta.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(): User? = repo.getCurrentUser()
}