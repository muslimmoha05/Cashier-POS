package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utopiatechhub.cashierpos.data.Cake
import kotlinx.coroutines.flow.Flow

@Dao
interface CakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cakes: List<Cake>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cake: Cake)

    @Update
    suspend fun update(cake: Cake)

    @Delete
    suspend fun delete(cake: Cake)

    @Query("SELECT * FROM cake_table")
    fun getAllCakes(): Flow<List<Cake>>

    @Insert
    suspend fun insertCakeManually(cake: List<Cake>)

    @Query("DELETE FROM sqlite_sequence WHERE name = 'cake_table'")
    suspend fun resetPrimaryKey()
}