package com.utopiatechhub.cashierpos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manager_table")
class Manager (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "manager_name") val managerName: String,
    @ColumnInfo(name = "hired_date") val hiredDate: String
)