package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utopiatechhub.cashierpos.data.PackedFood
import kotlinx.coroutines.flow.Flow

@Dao
interface PackedFoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(packedFood: PackedFood)

    @Insert
    suspend fun insertPackedFoodsManually(packedFoods: List<PackedFood>)

    @Query("SELECT * FROM packed_food_table")
    fun getAllPackedFoods(): Flow<List<PackedFood>>

    @Update
    suspend fun update(packedFood: PackedFood)

    @Delete
    suspend fun delete(packedFood: PackedFood)

    @Query("DELETE FROM packed_food_table")
    suspend fun deleteAllPackedFoods()

    @Query("DELETE FROM sqlite_sequence WHERE name = 'packed_food_table'")
    suspend fun resetPrimaryKey()

}