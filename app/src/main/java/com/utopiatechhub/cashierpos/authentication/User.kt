package com.utopiatechhub.cashierpos.authentication

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users",
    indices = [Index(value = ["username"], unique = true)]
)
data class User (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val businessName: String,
    val username: String,
    val password: String,
    val role: String,
    val tenantId: Long
)