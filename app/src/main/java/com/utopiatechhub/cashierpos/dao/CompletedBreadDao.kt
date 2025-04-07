package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.utopiatechhub.cashierpos.data.CompletedBread
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedBreadDao {
    @Insert
    suspend fun insert(completedBread: CompletedBread)

    @Delete
    suspend fun deleteCompletedBread(completedBread: CompletedBread)

    @Query("DELETE FROM completed_bread")
    suspend fun clearCompletedBreads()

    @Query("SELECT * FROM completed_bread")
    fun getAllCompletedBreads(): Flow<List<CompletedBread>>

    @Query("SELECT SUM(totalPrice) FROM completed_bread")
    suspend fun getTotalBreadPrice(): Double

    @Query("SELECT SUM(quantity) FROM completed_bread WHERE itemName = :itemName")
    suspend fun getBreadCount(itemName: String): Int

}