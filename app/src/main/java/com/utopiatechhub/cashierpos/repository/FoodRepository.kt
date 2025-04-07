package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.FoodDao
import com.utopiatechhub.cashierpos.data.Food
import com.utopiatechhub.cashierpos.data.FoodWithCategory
import kotlinx.coroutines.flow.Flow

class FoodRepository(private val foodDao: FoodDao) {

    val allFoods: Flow<List<FoodWithCategory>> = foodDao.getAllFoods()

    suspend fun insertFood(food: Food) {
        foodDao.insert(food)
    }

    suspend fun updateFood(food: Food) {
        foodDao.update(food)
    }

    suspend fun deleteFood(food: Food) {
        foodDao.delete(food)
    }

    fun getFoodsByCategory(foodCategoryId: Long): Flow<List<FoodWithCategory>> {
        return foodDao.getFoodsByCategory(foodCategoryId)
    }

    suspend fun addFoodManually(food: List<Food>) {
        foodDao.insertFoodManually(food)
    }
}
