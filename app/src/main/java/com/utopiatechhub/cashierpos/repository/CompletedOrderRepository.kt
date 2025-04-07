package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.CompletedOrderDao
import com.utopiatechhub.cashierpos.dao.OrderDao
import com.utopiatechhub.cashierpos.data.CompletedOrder
import kotlinx.coroutines.flow.Flow

class CompletedOrderRepository(
    private val completedOrderDao: CompletedOrderDao,
    private val orderDao: OrderDao
) {
    suspend fun insertCompletedOrder(completedOrder: CompletedOrder) {
        completedOrderDao.insertCompletedOrder(completedOrder)
    }

    suspend fun deleteCompletedOrder(completedOrder: CompletedOrder) {
        completedOrderDao.deleteCompletedOrder(completedOrder)
    }

    suspend fun clearCompletedOrders() {
        completedOrderDao.clearCompletedOrders()
        orderDao.clearOrders()
        orderDao.resetPrimaryKey()
    }

    fun getAllCompletedOrders(): Flow<List<CompletedOrder>> {
        return completedOrderDao.getAllCompletedOrders()
    }

    suspend fun getFoodCount(foodName: String): Int {
        return completedOrderDao.getFoodCount(foodName)
    }

    fun getTotalFoodPrice(): Double? {
        return completedOrderDao.getTotalFoodPrice()
    }

}