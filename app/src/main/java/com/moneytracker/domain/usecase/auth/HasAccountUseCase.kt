package com.moneytracker.domain.usecase.auth

import com.moneytracker.domain.repository.UserRepository
import javax.inject.Inject

class HasAccountUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(): Boolean = repo.hasAccount()
}