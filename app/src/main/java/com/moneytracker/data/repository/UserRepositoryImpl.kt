package com.moneytracker.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.moneytracker.data.local.dao.UserDao
import com.moneytracker.data.mapper.toDomain
import com.moneytracker.data.mapper.toEntity
import com.moneytracker.domain.model.User
import com.moneytracker.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
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
            userDao.clearAllLoginStatus()
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

            // Mark user as logged in after clearing any previous sessions
            userDao.clearAllLoginStatus()
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
            val firebaseUser =
                authResult.user ?: return Result.failure(Exception("Google sign-in failed"))

            val email = firebaseUser.email ?: ""
            val photoUrl = firebaseUser.photoUrl?.toString()
            val displayName = firebaseUser.displayName ?: "User"
            userDao.clearAllLoginStatus()
            var userEntity = userDao.getUserByEmail(email.lowercase())

            if (userEntity == null) {
                // Create new user if not exists
                val newUser = User(
                    name = displayName,
                    email = email,
                    firebaseUid = firebaseUser.uid,
                    profilePictureUrl = photoUrl,
                    isLoggedIn = true
                )
                userDao.insertUser(newUser.toEntity())
                userEntity = userDao.getUserByEmail(email.lowercase())
            } else {
                // Update existing user with firebaseUid and photoUrl
                if (userEntity.firebaseUid == null) {
                    userDao.updateFirebaseUid(userEntity.id, firebaseUser.uid)
                }
                if (!photoUrl.isNullOrBlank()) {
                    userDao.updateProfilePicture(userEntity.id, photoUrl)
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
        // Sign out all sessions in Room
        userDao.clearAllLoginStatus()
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

    override suspend fun updateProfilePicture(userId: Int, url: String?) =
        userDao.updateProfilePicture(userId, url)

    override suspend fun syncGoogleProfile(): Result<User> {
        return try {
            val firebaseUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("No Google account currently connected"))

            val current = userDao.getLoggedInUser()
                ?: return Result.failure(Exception("No active user session"))

            val photoUrl = firebaseUser.photoUrl?.toString()
            val displayName = firebaseUser.displayName

            if (!photoUrl.isNullOrBlank()) {
                userDao.updateProfilePicture(current.id, photoUrl)
            }
            if (!displayName.isNullOrBlank() && current.name.isBlank()) {
                userDao.updateProfile(
                    current.id,
                    displayName,
                    current.email,
                    current.phone,
                    current.occupation,
                    current.incomeGoal,
                    current.budgetLimit
                )
            }

            val updated = userDao.getCurrentUser()?.toDomain()
                ?: return Result.failure(Exception("Failed to load user"))
            Result.success(updated)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
