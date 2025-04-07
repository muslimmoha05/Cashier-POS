package com.utopiatechhub.cashierpos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_category_table")
data class FoodCategory (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "category_name") val categoryName: String,
    @ColumnInfo(name = "translated_name") val translatedName: String,
    @ColumnInfo(name = "photo_url") val photoUrl: String
)