package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.utopiatechhub.cashierpos.data.Waiter
import kotlinx.coroutines.flow.Flow

@Dao
interface WaiterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(waiters: List<Waiter>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(waiter: Waiter)

    @Delete
    suspend fun delete(waiter: Waiter)

    @Query("SELECT * FROM waiter_table")
    fun getAllWaiters(): Flow<List<Waiter>>
}