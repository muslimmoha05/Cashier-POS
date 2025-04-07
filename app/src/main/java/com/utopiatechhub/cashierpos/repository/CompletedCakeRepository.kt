package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.CompletedCakeDao
import com.utopiatechhub.cashierpos.data.CompletedCake
import kotlinx.coroutines.flow.Flow

class CompletedCakeRepository(private val completedCakeDao: CompletedCakeDao) {
    val allCompletedCakes: Flow<List<CompletedCake>> = completedCakeDao.getAllCompletedCakes()

    suspend fun insert(completedCake: CompletedCake) {
        completedCakeDao.insert(completedCake)
    }

    suspend fun delete(completedCake: CompletedCake) {
        completedCakeDao.delete(completedCake)
    }

    suspend fun clearCompletedCakes() {
        completedCakeDao.clearCompletedCakes()
    }

    suspend fun getTotalCakePrice(): Double {
        return completedCakeDao.getTotalCakePrice()
    }

    suspend fun getCakeCount(cakeName: String): Int {
        return completedCakeDao.getCakeCount(cakeName)
    }

}