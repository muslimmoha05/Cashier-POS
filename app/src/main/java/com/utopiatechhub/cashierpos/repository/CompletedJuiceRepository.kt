package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.CompletedJuiceDao
import com.utopiatechhub.cashierpos.data.CompletedJuice
import kotlinx.coroutines.flow.Flow

class CompletedJuiceRepository(private val completedJuiceDao: CompletedJuiceDao) {

    suspend fun insert(completedJuice: CompletedJuice) {
        completedJuiceDao.insert(completedJuice)
    }

    suspend fun deleteCompletedJuice(completedJuice: CompletedJuice) {
        completedJuiceDao.deleteCompletedJuice(completedJuice)
    }

    suspend fun clearCompletedJuices() {
        completedJuiceDao.clearCompletedJuices()
    }

    fun getAllCompletedJuices(): Flow<List<CompletedJuice>> {
        return completedJuiceDao.getAllCompletedJuices()
    }

    suspend fun getJuiceCount(itemName: String): Int {
        return completedJuiceDao.getJuiceCount(itemName)
    }

    suspend fun getTotalJuicePrice(): Double {
        return completedJuiceDao.getTotalJuicePrice()
    }
}