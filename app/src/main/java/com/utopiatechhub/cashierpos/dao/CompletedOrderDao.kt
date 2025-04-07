package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utopiatechhub.cashierpos.data.CompletedOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedOrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletedOrder(completedOrder: CompletedOrder)

    @Delete
    suspend fun deleteCompletedOrder(completedOrder: CompletedOrder)

    @Query("DELETE FROM completed_order_table")
    suspend fun clearCompletedOrders()

    @Query("SELECT * FROM completed_order_table")
    fun getAllCompletedOrders(): Flow<List<CompletedOrder>>

    @Query("SELECT SUM(quantity) FROM completed_order_table WHERE food_name = :foodName")
    suspend fun getFoodCount(foodName: String): Int

    @Query("SELECT SUM(total_price) FROM completed_order_table")
    fun getTotalFoodPrice(): Double?
}