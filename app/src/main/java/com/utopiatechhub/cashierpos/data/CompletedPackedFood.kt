package com.utopiatechhub.cashierpos.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completed_packed_food")
data class CompletedPackedFood (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val packedFoodName: String,
    val packedFoodPrice: Double,
    val packedFoodQuantity: Int,
    val packedFoodTotal: Double,
    val packedFoodDate: String
)