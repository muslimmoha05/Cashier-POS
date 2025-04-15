package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.JuiceDao
import com.utopiatechhub.cashierpos.data.Juice
import kotlinx.coroutines.flow.Flow

class JuiceRepository(private val juiceDao: JuiceDao) {

    val allJuices: Flow<List<Juice>> = juiceDao.getAllJuices()

    suspend fun insertJuice(juice: Juice) {
        juiceDao.insert(juice)
    }

    suspend fun updateJuice(juice: Juice) {
        juiceDao.update(juice)
    }

    suspend fun deleteJuice(juice: Juice) {
        juiceDao.delete(juice)
    }

    suspend fun deleteAllJuices() {
        juiceDao.deleteAllJuices()
        juiceDao.resetPrimaryKey()
    }

    suspend fun insertJuicesManually(juices: List<Juice>) {
        juiceDao.insertJuicesManually(juices)
    }
}