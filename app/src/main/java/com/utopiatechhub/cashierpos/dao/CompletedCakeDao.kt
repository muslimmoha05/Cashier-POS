package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.utopiatechhub.cashierpos.data.CompletedCake
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedCakeDao {
    @Insert
    suspend fun insert(completedCake: CompletedCake)

    @Query("SELECT * FROM completed_cake")
    fun getAllCompletedCakes(): Flow<List<CompletedCake>>

    @Delete
    suspend fun delete(completedCake: CompletedCake)

    @Query("DELETE FROM completed_cake")
    suspend fun clearCompletedCakes()

    @Query("SELECT SUM(totalPrice) FROM completed_cake")
    suspend fun getTotalCakePrice(): Double

    @Query("SELECT SUM(quantity) FROM completed_cake WHERE itemName = :cakeName")
    suspend fun getCakeCount(cakeName: String): Int
}