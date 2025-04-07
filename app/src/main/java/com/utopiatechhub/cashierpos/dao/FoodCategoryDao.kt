package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.utopiatechhub.cashierpos.data.FoodCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(foodCategories: List<FoodCategory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: FoodCategory)

    @Update
    suspend fun update(category: FoodCategory)

    @Delete
    suspend fun delete(category: FoodCategory)

    // Fetch all categories ordered alphabetically
    @Query("SELECT * FROM food_category_table")
    fun getAllCategories(): Flow<List<FoodCategory>>

    // Fetch category by ID (for admin editing)
    @Query("SELECT * FROM food_category_table WHERE id = :foodCategoryId LIMIT 1")
    suspend fun getCategoryById(foodCategoryId: Long): FoodCategory?

    @Insert
    suspend fun insertCategoryManually(category: FoodCategory)
}
