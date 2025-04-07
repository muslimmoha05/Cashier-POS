package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.OrderDao
import com.utopiatechhub.cashierpos.data.Order
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val orderDao: OrderDao) {
    suspend fun insertOrder(order: Order) {
        orderDao.insertOrder(order)
    }

    fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAllOrders()
    }

    suspend fun deleteOrder(order: Order) {
        orderDao.deleteOrderById(order.id)
    }

    suspend fun clearOrders() {
        orderDao.clearOrders()
    }
}