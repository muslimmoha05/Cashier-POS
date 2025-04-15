package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.FoodCategoryDao
import com.utopiatechhub.cashierpos.data.FoodCategory
import kotlinx.coroutines.flow.Flow

class FoodCategoryRepository(private val foodCategoryDao: FoodCategoryDao) {

    val allCategories: Flow<List<FoodCategory>> = foodCategoryDao.getAllCategories()

    suspend fun insertCategory(foodCategory: FoodCategory) {
        foodCategoryDao.insert(foodCategory)
    }

    suspend fun updateCategory(foodCategory: FoodCategory) {
        foodCategoryDao.update(foodCategory)
    }

    suspend fun deleteCategory(foodCategory: FoodCategory) {
        foodCategoryDao.delete(foodCategory)
    }

    suspend fun deleteAllCategories() {
        foodCategoryDao.deleteAllCategories()
        foodCategoryDao.resetPrimaryKey()
    }

    suspend fun getCategoryById(categoryId: Long): FoodCategory? {
        return foodCategoryDao.getCategoryById(categoryId)
    }

    suspend fun addCategoryManually(category: FoodCategory) {
        foodCategoryDao.insertCategoryManually(category)
    }
}
