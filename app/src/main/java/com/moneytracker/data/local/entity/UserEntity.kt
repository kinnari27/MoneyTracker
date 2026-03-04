package com.moneytracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val occupation: String,
    val incomeGoal: String,
    val budgetLimit: String,
    val currency: String,
    val biometricEnabled: Boolean
)
