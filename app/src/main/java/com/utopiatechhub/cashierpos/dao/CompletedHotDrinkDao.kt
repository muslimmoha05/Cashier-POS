package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.utopiatechhub.cashierpos.data.CompletedHotDrink
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedHotDrinkDao {
    @Insert
    suspend fun insert(completedHotDrink: CompletedHotDrink)

    @Query("SELECT * FROM completed_hot_drink")
    fun getAllCompletedHotDrinks(): Flow<List<CompletedHotDrink>>

    @Delete
    suspend fun deleteCompletedHotDrink(completedHotDrink: CompletedHotDrink)

    @Query("DELETE FROM completed_hot_drink")
    suspend fun clearCompletedHotDrinks()

    @Query("SELECT SUM(quantity) FROM completed_hot_drink WHERE itemName = :itemName")
    suspend fun getHotDrinkCount(itemName: String): Int

    @Query("SELECT SUM(totalPrice) FROM completed_hot_drink")
    suspend fun getTotalHotDrinkPrice(): Double

}