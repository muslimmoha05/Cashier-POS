package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.CompletedPackedFoodDao
import com.utopiatechhub.cashierpos.data.CompletedPackedFood
import kotlinx.coroutines.flow.Flow

class CompletedPackedFoodRepository(private val completedPackedFoodDao: CompletedPackedFoodDao) {

    suspend fun insert(completedPackedFood: CompletedPackedFood) {
        completedPackedFoodDao.insert(completedPackedFood)
    }

    suspend fun delete(completedPackedFood: CompletedPackedFood) {
        completedPackedFoodDao.delete(completedPackedFood)
    }

    suspend fun clearAllCompletedPackedFoods() {
        completedPackedFoodDao.clearAllCompletedPackedFoods()
    }

    fun getAllCompletedPackedFoods(): Flow<List<CompletedPackedFood>> {
        return completedPackedFoodDao.getAllCompletedPackedFoods()
    }

    suspend fun getTotalCompletedPackedFoodPrice(): Double {
        return completedPackedFoodDao.getTotalCompletedPackedFoodPrice()
    }

    suspend fun getCompletedPackedFoodCount(packedFoodName: String): Int {
        return completedPackedFoodDao.getCompletedPackedFoodCount(packedFoodName)
    }

}