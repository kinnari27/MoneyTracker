package com.moneytracker.data.repository

import com.moneytracker.data.local.dao.UserDao
import com.moneytracker.data.mapper.toDomain
import com.moneytracker.data.mapper.toEntity
import com.moneytracker.domain.model.User
import com.moneytracker.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.map
import java.security.MessageDigest
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val firebaseAuth: FirebaseAuth
) : UserRepository {

    // ── Observe (reactive) ────────────────────────────────────────────────────
    override val observeCurrentUser: Flow<User?> =
        userDao.observeCurrentUser().map { it?.toDomain() }

    // ── One-shot reads ────────────────────────────────────────────────────────
    override suspend fun getCurrentUser(): User? =
        userDao.getCurrentUser()?.toDomain()

    override suspend fun hasAccount(): Boolean =
        userDao.getUserCount() > 0

    override suspend fun isUserLoggedIn(): Boolean =
        userDao.getLoggedInUser() != null

    // ── Auth ──────────────────────────────────────────────────────────────────
    override suspend fun registerUser(user: User, password: String): Result<Unit> {
        return try {
            val existing = userDao.getUserByEmail(user.email.trim().lowercase())
            if (existing != null) {
                return Result.failure(Exception("An account with this email already exists"))
            }
            // New user is automatically logged in
            val newUser = user.copy(isLoggedIn = true)
            userDao.insertUser(newUser.toEntity(hashedPin = hashPassword(password)))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val entity = userDao.getUserByEmail(email.trim().lowercase())
                ?: return Result.failure(Exception("No account found with this email"))

            if (entity.pin != hashPassword(password)) {
                return Result.failure(Exception("Incorrect password"))
            }

            // Mark user as logged in
            userDao.updateLoginStatus(entity.id, true)

            Result.success(entity.toDomain().copy(isLoggedIn = true))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user ?: return Result.failure(Exception("Google sign-in failed"))

            val email = firebaseUser.email ?: ""
            var userEntity = userDao.getUserByEmail(email.lowercase())

            if (userEntity == null) {
                // Create new user if not exists
                val newUser = User(
                    name = firebaseUser.displayName ?: "User",
                    email = email,
                    firebaseUid = firebaseUser.uid,
                    isLoggedIn = true
                )
                userDao.insertUser(newUser.toEntity())
                userEntity = userDao.getUserByEmail(email.lowercase())
            } else {
                // Update existing user with firebaseUid if missing and set login status
                if (userEntity.firebaseUid == null) {
                    userDao.updateFirebaseUid(userEntity.id, firebaseUser.uid)
                }
                userDao.updateLoginStatus(userEntity.id, true)
                // Refresh entity after update
                userEntity = userDao.getUserByEmail(email.lowercase())
            }

            Result.success(userEntity!!.toDomain().copy(isLoggedIn = true))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        // Sign out from Firebase
        firebaseAuth.signOut()
        // Find current logged in user and sign them out in Room
        val user = userDao.getLoggedInUser()
        if (user != null) {
            userDao.updateLoginStatus(user.id, false)
        }
    }

    // ── Profile updates ───────────────────────────────────────────────────────
    override suspend fun updateProfile(
        userId: Int,
        name: String,
        email: String,
        phone: String,
        occupation: String,
        incomeGoal: Double,
        budgetLimit: Double
    ) = userDao.updateProfile(userId, name, email, phone, occupation, incomeGoal, budgetLimit)

    override suspend fun updateCurrency(userId: Int, code: String) =
        userDao.updateCurrency(userId, code)

    // ── Security ──────────────────────────────────────────────────────────────
    override suspend fun updatePin(userId: Int, newPin: String): Result<Unit> {
        return try {
            userDao.updatePin(userId, hashPassword(newPin))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBiometric(userId: Int, enabled: Boolean) =
        userDao.updateBiometric(userId, enabled)

    // ── Insert ────────────────────────────────────────────────────────────────
    override suspend fun insertUser(user: User) {
        userDao.insertUser(user.toEntity())
    }

    // ── Private helpers ───────────────────────────────────────────────────────
    /** SHA-256 — passwords are never stored in plain text */
    private fun hashPassword(input: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }
}
