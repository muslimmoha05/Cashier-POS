package com.utopiatechhub.cashierpos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "juice_table")
data class Juice (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "juice_name") val juiceName: String,
    @ColumnInfo(name = "juice_price") val juicePrice: Double,
)