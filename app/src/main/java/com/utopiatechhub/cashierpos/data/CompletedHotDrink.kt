package com.utopiatechhub.cashierpos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completed_hot_drink")
data class CompletedHotDrink (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val waiterName: String,
    val itemName: String,
    val quantity: Int,
    val totalPrice: Double,
    val orderDate: String
)