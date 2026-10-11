package com.moneytracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moneytracker.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun observeCurrentUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("UPDATE users SET biometricEnabled = :enabled WHERE id = :userId")
    suspend fun updateBiometric(userId: Int, enabled: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()

    @Query("DELETE FROM budgets")
    suspend fun deleteAllBudgets()

    @Query("UPDATE users SET name = :name, email = :email, phone = :phone, occupation = :occupation, incomeGoal = :incomeGoal, budgetLimit = :budgetLimit WHERE id = :userId")
    suspend fun updateProfile(
        userId: Int,
        name: String,
        email: String,
        phone: String,
        occupation: String,
        incomeGoal: Double,
        budgetLimit: Double
    )

    @Query("UPDATE users SET currency = :code WHERE id = :userId")
    suspend fun updateCurrency(userId: Int, code: String)

    @Query("UPDATE users SET profilePictureUrl = :url WHERE id = :userId")
    suspend fun updateProfilePicture(userId: Int, url: String?)

    @Query("UPDATE users SET pin = :newPin WHERE id = :userId")
    suspend fun updatePin(userId: Int, newPin: String)

    @Query("UPDATE users SET isLoggedIn = :isLoggedIn WHERE id = :userId")
    suspend fun updateLoginStatus(userId: Int, isLoggedIn: Boolean)

    @Query("UPDATE users SET firebaseUid = :uid WHERE id = :userId")
    suspend fun updateFirebaseUid(userId: Int, uid: String)

    @Query("UPDATE users SET isLoggedIn = 0")
    suspend fun clearAllLoginStatus()

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUser(): UserEntity?
}
