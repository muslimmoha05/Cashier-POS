package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.OtherOrder
import com.utopiatechhub.cashierpos.repository.OtherOrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OtherOrderViewModel(private val otherOrderRepository: OtherOrderRepository) : ViewModel() {

    private val _otherOrders = MutableStateFlow<List<OtherOrder>>(emptyList())
    val otherOrders: StateFlow<List<OtherOrder>> get() = _otherOrders

    fun fetchOtherOrders() {
        viewModelScope.launch {
            otherOrderRepository.getOtherOrders()
                .collect { orders ->
                    _otherOrders.value = orders
                }
        }
    }

    fun deleteOtherOrder(order: OtherOrder) {
        viewModelScope.launch {
            otherOrderRepository.deleteOtherOrder(order)
        }
    }

    fun insertOtherOrder(otherOrder: OtherOrder) {
        viewModelScope.launch {
            otherOrderRepository.insertOtherOrder(otherOrder)
        }
    }
}