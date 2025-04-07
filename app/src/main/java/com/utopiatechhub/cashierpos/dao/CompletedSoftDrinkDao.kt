package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utopiatechhub.cashierpos.data.CompletedSoftDrink
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedSoftDrinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(completedSoftDrink: CompletedSoftDrink)

    @Delete
    suspend fun delete(completedSoftDrink: CompletedSoftDrink)

    @Query("DELETE FROM completed_soft_drink")
    suspend fun clearCompletedSoftDrinks()

    @Query("SELECT * FROM completed_soft_drink")
    fun getAllCompletedSoftDrinks(): Flow<List<CompletedSoftDrink>>

    @Query("SELECT SUM(quantity) FROM completed_soft_drink WHERE itemName = :itemName")
    suspend fun getSoftDrinkCount(itemName: String): Int

    @Query("SELECT SUM(totalPrice) FROM completed_soft_drink")
    suspend fun getTotalSoftDrinkPrice(): Double
}