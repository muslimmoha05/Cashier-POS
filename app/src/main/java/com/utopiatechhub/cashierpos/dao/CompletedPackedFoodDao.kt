package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.utopiatechhub.cashierpos.data.CompletedPackedFood
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedPackedFoodDao {
    @Insert
    suspend fun insert(completedPackedFood: CompletedPackedFood)

    @Delete
    suspend fun delete(completedPackedFood: CompletedPackedFood)

    @Query("DELETE FROM completed_packed_food")
    suspend fun clearAllCompletedPackedFoods()

    @Query("SELECT * FROM completed_packed_food")
    fun getAllCompletedPackedFoods(): Flow<List<CompletedPackedFood>>

    @Query("SELECT SUM(packedFoodTotal) FROM completed_packed_food")
    suspend fun getTotalCompletedPackedFoodPrice(): Double

    @Query("SELECT SUM(packedFoodQuantity) FROM completed_packed_food WHERE packedFoodName = :packedFoodName")
    suspend fun getCompletedPackedFoodCount(packedFoodName: String): Int

}