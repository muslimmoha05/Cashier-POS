package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utopiatechhub.cashierpos.data.SoftDrink
import kotlinx.coroutines.flow.Flow

@Dao
interface SoftDrinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(softDrink: SoftDrink)

    @Insert
    suspend fun insertSoftDrinksManually(softDrinks: List<SoftDrink>)

    @Query("SELECT * FROM soft_drink_table")
    fun getAllSoftDrinks(): Flow<List<SoftDrink>>

    @Update
    suspend fun update(softDrink: SoftDrink)

    @Delete
    suspend fun delete(softDrink: SoftDrink)

    @Query("DELETE FROM soft_drink_table")
    suspend fun deleteAllSoftDrinks()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'soft_drink_table'")
    suspend fun resetPrimaryKey()
}