package com.utopiatechhub.cashierpos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_table",
    foreignKeys = [
        ForeignKey(
            entity = FoodCategory::class,
            parentColumns = ["id"],
            childColumns = ["food_category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["food_category_id"], name = "index_food_category_id")]
)
data class Food (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "food_name") val foodName: String,
    @ColumnInfo(name = "food_category_id") val foodCategoryId: Long,
    @ColumnInfo(name = "food_price") val foodPrice: Double
)