package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utopiatechhub.cashierpos.data.Manager
import kotlinx.coroutines.flow.Flow

@Dao
interface ManagerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(manager: Manager)

    @Delete
    suspend fun delete(manager: Manager)

    @Query("SELECT * FROM manager_table")
    fun getAllManagers(): Flow<List<Manager>>

}