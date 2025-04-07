package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.utopiatechhub.cashierpos.data.CompletedJuice
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedJuiceDao {
    @Insert
    suspend fun insert(completedJuice: CompletedJuice)

    @Delete
    suspend fun deleteCompletedJuice(completedJuice: CompletedJuice)

    @Query("DELETE FROM completed_juice")
    suspend fun clearCompletedJuices()

    @Query("SELECT * FROM completed_juice")
    fun getAllCompletedJuices(): Flow<List<CompletedJuice>>

    @Query("SELECT SUM(quantity) FROM completed_juice WHERE itemName = :itemName")
    suspend fun getJuiceCount(itemName: String): Int

    @Query("SELECT SUM(totalPrice) FROM completed_juice")
    suspend fun getTotalJuicePrice(): Double
}