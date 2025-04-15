package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utopiatechhub.cashierpos.data.Juice
import kotlinx.coroutines.flow.Flow

@Dao
interface JuiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(juice: Juice)

    @Insert
    suspend fun insertJuicesManually(juices: List<Juice>)

    @Query("SELECT * FROM juice_table")
    fun getAllJuices(): Flow<List<Juice>>

    @Update
    suspend fun update(juice: Juice)

    @Delete
    suspend fun delete(juice: Juice)

    @Query("DELETE FROM juice_table")
    suspend fun deleteAllJuices()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'juice_table'")
    suspend fun resetPrimaryKey()
}