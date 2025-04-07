package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.ManagerDao
import com.utopiatechhub.cashierpos.data.Manager
import kotlinx.coroutines.flow.Flow

class ManagerRepository(private val managerDao: ManagerDao) {
    suspend fun insert(manager: Manager) {
        managerDao.insert(manager)
    }

    suspend fun delete(manager: Manager) {
        managerDao.delete(manager)
    }

    fun getAllManagers(): Flow<List<Manager>> {
        return managerDao.getAllManagers()
    }
}