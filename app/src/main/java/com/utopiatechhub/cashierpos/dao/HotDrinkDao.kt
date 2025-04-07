package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utopiatechhub.cashierpos.data.HotDrink
import kotlinx.coroutines.flow.Flow

@Dao
interface HotDrinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hotDrink: HotDrink)

    @Insert
    suspend fun insertHotDrinksManually(hotDrink: List<HotDrink>)

    @Query("SELECT * FROM hot_drink_table")
    fun getAllHotDrinks(): Flow<List<HotDrink>>

    @Update
    suspend fun update(hotDrink: HotDrink)

    @Delete
    suspend fun delete(hotDrink: HotDrink)

    @Query("DELETE FROM sqlite_sequence WHERE name = 'hot_drink_table'")
    suspend fun resetPrimaryKey()

}