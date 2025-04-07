package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.CompletedOrderRepository
import com.utopiatechhub.cashierpos.viewmodel.CompletedOrderViewModel

class CompletedOrderViewModelFactory(private val completedOrderRepository: CompletedOrderRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompletedOrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CompletedOrderViewModel(completedOrderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}