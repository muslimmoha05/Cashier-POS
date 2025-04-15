package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utopiatechhub.cashierpos.data.Bread
import kotlinx.coroutines.flow.Flow

@Dao
interface BreadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bread: Bread)

    @Insert
    suspend fun insertBreadsManually(breads: List<Bread>)

    @Query("SELECT * FROM bread_table")
    fun getAllBreads(): Flow<List<Bread>>

    @Query("DELETE FROM bread_table")
    suspend fun deleteAllBreads()

    @Update
    suspend fun update(bread: Bread)

    @Delete
    suspend fun delete(bread: Bread)

    @Query("DELETE FROM sqlite_sequence WHERE name = 'bread_table'")
    suspend fun resetPrimaryKey()
}