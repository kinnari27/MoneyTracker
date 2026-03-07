package com.moneytracker.domain.usecase.auth

import com.moneytracker.domain.repository.UserRepository
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(): Boolean = repo.isUserLoggedIn()
}
