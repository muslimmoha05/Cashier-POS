package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.PackedFoodDao
import com.utopiatechhub.cashierpos.data.PackedFood
import kotlinx.coroutines.flow.Flow

class PackedFoodRepository(private val  packedFoodDao: PackedFoodDao) {

    val allPackedFoods: Flow<List<PackedFood>> = packedFoodDao.getAllPackedFoods()

    suspend fun insertPackedFood(packedFood: PackedFood) {
        packedFoodDao.insert(packedFood)
    }

    suspend fun updatePackedFood(packedFood: PackedFood) {
        packedFoodDao.update(packedFood)
    }

    suspend fun deletePackedFood(packedFood: PackedFood) {
        packedFoodDao.delete(packedFood)
    }

    suspend fun deleteAllPackedFoods() {
        packedFoodDao.deleteAllPackedFoods()
        packedFoodDao.resetPrimaryKey()
    }

    suspend fun insertPackedFoodsManually(packedFoods: List<PackedFood>) {
        packedFoodDao.insertPackedFoodsManually(packedFoods)
    }
}