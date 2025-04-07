package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.CompletedBreadDao
import com.utopiatechhub.cashierpos.data.CompletedBread
import kotlinx.coroutines.flow.Flow

class CompletedBreadRepository(private val completedBreadDao: CompletedBreadDao) {

    suspend fun insert(completedBread: CompletedBread) {
        completedBreadDao.insert(completedBread)
    }

    suspend fun deleteCompletedBread(completedBread: CompletedBread) {
        completedBreadDao.deleteCompletedBread(completedBread)
    }

    suspend fun clearCompletedBreads() {
        completedBreadDao.clearCompletedBreads()
    }

    fun getAllCompletedBreads(): Flow<List<CompletedBread>> {
        return completedBreadDao.getAllCompletedBreads()
    }

    suspend fun getTotalBreadPrice(): Double {
        return completedBreadDao.getTotalBreadPrice()
    }

    suspend fun getBreadCount(itemName: String): Int {
        return completedBreadDao.getBreadCount(itemName)
    }

}