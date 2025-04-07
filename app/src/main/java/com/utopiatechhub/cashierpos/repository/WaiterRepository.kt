package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.WaiterDao
import com.utopiatechhub.cashierpos.data.Waiter
import kotlinx.coroutines.flow.Flow

class WaiterRepository(private val waiterDao: WaiterDao) {
    suspend fun insert(waiter: Waiter) {
        waiterDao.insert(waiter)
    }

    suspend fun delete(waiter: Waiter) {
        waiterDao.delete(waiter)
    }

    fun getAllWaiters(): Flow<List<Waiter>> {
        return waiterDao.getAllWaiters()
    }
}