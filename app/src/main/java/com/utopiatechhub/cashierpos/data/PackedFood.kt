package com.utopiatechhub.cashierpos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "packed_food_table")
data class PackedFood (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "packed_food_name") val packedFoodName: String,
    @ColumnInfo(name = "packed_food_price") val packedFoodPrice: Double
)