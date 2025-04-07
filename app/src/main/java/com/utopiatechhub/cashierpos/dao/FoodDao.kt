package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.utopiatechhub.cashierpos.data.Food
import com.utopiatechhub.cashierpos.data.FoodWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(foods: List<Food>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: Food)

    @Update
    suspend fun update(food: Food)

    @Delete
    suspend fun delete(food: Food)

    // Fetch all foods with their category name
    @Query("""
        SELECT 
            food_table.id AS id, 
            food_table.food_name AS foodName, 
            food_table.food_price AS foodPrice, 
            food_category_table.id AS foodCategoryId,
            food_category_table.category_name AS categoryName 
        FROM food_table
        INNER JOIN food_category_table 
            ON food_table.food_category_id = food_category_table.id
    """)
    fun getAllFoods(): Flow<List<FoodWithCategory>>

    // Fetch foods by category ID (for filtering in the client side)
    @Query("""
        SELECT 
            food_table.id AS id, 
            food_table.food_name AS foodName, 
            food_table.food_price AS foodPrice, 
            food_category_table.id AS foodCategoryId,
            food_category_table.category_name AS categoryName 
        FROM food_table
        INNER JOIN food_category_table ON food_table.food_category_id = food_category_table.id
        WHERE food_table.food_category_id = :foodCategoryId
    """)
    fun getFoodsByCategory(foodCategoryId: Long): Flow<List<FoodWithCategory>>

    @Insert
    suspend fun insertFoodManually(food: List<Food>)
}
