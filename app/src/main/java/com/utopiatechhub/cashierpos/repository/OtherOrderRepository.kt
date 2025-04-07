package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.OtherOrderDao
import com.utopiatechhub.cashierpos.data.OtherOrder
import kotlinx.coroutines.flow.Flow

class OtherOrderRepository(private val otherOrderDao: OtherOrderDao) {

    fun getOtherOrders(): Flow<List<OtherOrder>> {
        return otherOrderDao.getOtherOrders()
    }

    suspend fun deleteOtherOrder(order: OtherOrder) {
        otherOrderDao.delete(order)
    }

    suspend fun insertOtherOrder(otherOrder: OtherOrder) {
        otherOrderDao.insertOtherOrder(otherOrder)
    }
}