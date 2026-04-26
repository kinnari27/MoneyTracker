package com.moneytracker.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val firebaseUid: String? = null,
    val phone: String = "",
    val occupation: String = "",
    val incomeGoal: Double = 0.0,
    val budgetLimit: Double = 0.0,
    val currency: String = "INR",
    val pin: String = "",
    val biometricEnabled: Boolean = false,
    val isLoggedIn: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
