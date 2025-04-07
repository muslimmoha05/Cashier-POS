package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.CartDao
import com.utopiatechhub.cashierpos.data.Cart
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: CartDao) {
    suspend fun insertCartItem(cart: Cart) {
        cartDao.insertCartItem(cart)
    }

    fun getAllCartItems(): Flow<List<Cart>> {
        return cartDao.getAllCartItems()
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    suspend fun deleteCartItem(cart: Cart) {
        cartDao.deleteCartItem(cart)
    }
}