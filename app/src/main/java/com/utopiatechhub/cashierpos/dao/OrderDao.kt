package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utopiatechhub.cashierpos.data.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Query("DELETE FROM order_table WHERE id = :orderId")
    suspend fun deleteOrderById(orderId: Long)

    @Query("SELECT * FROM order_table")
    fun getAllOrders(): Flow<List<Order>>

    @Query("DELETE FROM order_table")
    suspend fun clearOrders()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'order_table'")
    suspend fun resetPrimaryKey()
}