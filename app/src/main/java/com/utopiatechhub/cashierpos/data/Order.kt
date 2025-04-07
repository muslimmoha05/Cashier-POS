package com.utopiatechhub.cashierpos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_table")
data class Order (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "waiter_name") val waiterName: String,
    @ColumnInfo(name = "food_name") val foodName: String,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "total_price") val totalPrice: Double,
    @ColumnInfo(name = "order_date") val orderDate: String
)