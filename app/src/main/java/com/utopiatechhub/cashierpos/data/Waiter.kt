package com.utopiatechhub.cashierpos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "waiter_table")
data class Waiter (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "waiter_name") val waiterName: String,
    @ColumnInfo(name = "hired_date") val hiredDate: String
)