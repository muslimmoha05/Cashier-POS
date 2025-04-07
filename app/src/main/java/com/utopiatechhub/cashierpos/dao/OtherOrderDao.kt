package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utopiatechhub.cashierpos.data.OtherOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface OtherOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOtherOrder(otherOrder: OtherOrder)

    @Query("SELECT * FROM other_order_table")
    fun getOtherOrders(): Flow<List<OtherOrder>>

    @Delete
    suspend fun delete(order: OtherOrder)
}