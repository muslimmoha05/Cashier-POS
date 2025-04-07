package com.utopiatechhub.cashierpos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "soft_drink_table")
data class SoftDrink (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "soft_drink_name") val softDrinkName: String,
    @ColumnInfo(name = "soft_drink_price") val softDrinkPrice: Double
)