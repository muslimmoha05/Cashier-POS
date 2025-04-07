package com.utopiatechhub.cashierpos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class Cart (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    // Food
    @ColumnInfo(name = "food_name") val foodName: String?,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "food_price") val foodPrice: Double,
    // Cake
    @ColumnInfo(name = "cake_name") val cakeName: String?,
    @ColumnInfo(name = "cake_quantity") val cakeQuantity: Int,
    @ColumnInfo(name = "cake_price") val cakePrice: Double,
    // Hot Drink
    @ColumnInfo(name = "hot_drink_name") val hotDrinkName: String?,
    @ColumnInfo(name = "hot_drink_quantity") val hotDrinkQuantity: Int,
    @ColumnInfo(name = "hot_drink_price") val hotDrinkPrice: Double,
    // Soft Drink
    @ColumnInfo(name = "soft_drink_name") val softDrinkName: String?,
    @ColumnInfo(name = "soft_drink_quantity") val softDrinkQuantity: Int,
    @ColumnInfo(name = "soft_drink_price") val softDrinkPrice: Double,
    // Bread
    @ColumnInfo(name = "bread_name") val breadName: String?,
    @ColumnInfo(name = "bread_quantity") val breadQuantity: Int,
    @ColumnInfo(name = "bread_price") val breadPrice: Double,
    // Juice
    @ColumnInfo(name = "juice_name") val juiceName: String?,
    @ColumnInfo(name = "juice_quantity") val juiceQuantity: Int,
    @ColumnInfo(name = "juice_price") val juicePrice: Double
)