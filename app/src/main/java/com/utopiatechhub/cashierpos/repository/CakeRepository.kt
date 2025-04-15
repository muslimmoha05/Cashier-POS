package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.CakeDao
import com.utopiatechhub.cashierpos.data.Cake
import kotlinx.coroutines.flow.Flow

class CakeRepository(private val cakeDao: CakeDao) {

    val allCakes: Flow<List<Cake>> = cakeDao.getAllCakes()

    suspend fun insertCake(cake: Cake) {
        cakeDao.insert(cake)
    }

    suspend fun updateCake(cake: Cake) {
        cakeDao.update(cake)
    }

    suspend fun deleteCake(cake: Cake) {
        cakeDao.delete(cake)
    }

    suspend fun deleteAllCakes() {
        cakeDao.deleteAllCakes()
        cakeDao.resetPrimaryKey()
    }

    suspend fun addCakeManually(cake: List<Cake>) {
        cakeDao.insertCakeManually(cake)
    }
}