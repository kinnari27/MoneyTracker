package com.moneytracker.domain.usecase.auth

import com.moneytracker.domain.model.User
import com.moneytracker.domain.repository.UserRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank()) return Result.failure(Exception("Please enter your email"))
        if (password.isBlank()) return Result.failure(Exception("Please enter your password"))
        return repo.loginUser(email, password)
    }
}