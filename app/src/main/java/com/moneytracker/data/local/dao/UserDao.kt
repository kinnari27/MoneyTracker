package com.moneytracker.data.local.dao

import androidx.room.*
import com.moneytracker.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    fun observeUser(): Flow<UserEntity?>

    @Query("UPDATE users SET biometricEnabled = :enabled WHERE id = :userId")
    suspend fun updateBiometric(userId: Int, enabled: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun deleteUser()
}
