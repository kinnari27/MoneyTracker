package com.moneytracker.domain.usecase.auth

import com.moneytracker.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class HasAccountUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Boolean {
        return userRepository.observeCurrentUser.firstOrNull() != null
    }
}