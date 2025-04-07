package com.utopiatechhub.cashierpos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bread_table")
data class Bread (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "bread_name") val breadName: String,
    @ColumnInfo(name = "bread_price") val breadPrice: Double
)