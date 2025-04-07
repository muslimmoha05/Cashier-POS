package com.utopiatechhub.cashierpos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hot_drink_table")
data class HotDrink (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "hot_drink_name") val hotDrinkName: String,
    @ColumnInfo(name = "hot_drink_price") val hotDrinkPrice: Double
)