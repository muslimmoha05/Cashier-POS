package com.utopiatechhub.cashierpos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completed_cake")
data class CompletedCake(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val waiterName: String,
    val itemName: String,
    val quantity: Int,
    val totalPrice: Double,
    val orderDate: String
)