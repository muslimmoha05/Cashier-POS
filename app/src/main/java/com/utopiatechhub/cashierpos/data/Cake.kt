package com.utopiatechhub.cashierpos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "cake_table")
data class Cake (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "cake_name") val cakeName: String,
    @ColumnInfo(name = "cake_price") val cakePrice: Double
)