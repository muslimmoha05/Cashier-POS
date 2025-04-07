package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.BreadDao
import com.utopiatechhub.cashierpos.data.Bread
import kotlinx.coroutines.flow.Flow

class BreadRepository(private val breadDao: BreadDao) {

    val allBreads: Flow<List<Bread>> = breadDao.getAllBreads()

    suspend fun insertBread(bread: Bread) {
        breadDao.insert(bread)
    }

    suspend fun updateBread(bread: Bread) {
        breadDao.update(bread)
    }

    suspend fun deleteBread(bread: Bread) {
        breadDao.delete(bread)
    }

    suspend fun insertBreadsManually(breads: List<Bread>) {
        breadDao.insertBreadsManually(breads)
    }

}