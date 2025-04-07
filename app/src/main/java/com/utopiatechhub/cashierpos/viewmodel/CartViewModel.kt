package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.Cart
import com.utopiatechhub.cashierpos.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<Cart>>(emptyList())
    val cartItems: StateFlow<List<Cart>> = _cartItems

    init {
        fetchCartItems()
    }

    fun addToCart(cart: Cart) {
        viewModelScope.launch {
            repository.insertCartItem(cart)
            fetchCartItems()
        }
    }

    fun fetchCartItems() {
        viewModelScope.launch {
            repository.getAllCartItems().collect { items ->
                _cartItems.value = items
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
            fetchCartItems()
        }
    }

    fun deleteCartItem(cart: Cart) {
        viewModelScope.launch {
            repository.deleteCartItem(cart)
            fetchCartItems()
        }
    }
}