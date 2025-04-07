package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.OtherOrderRepository
import com.utopiatechhub.cashierpos.viewmodel.OtherOrderViewModel

class OtherOrderViewModelFactory(private val otherOrderRepository: OtherOrderRepository) :
    ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OtherOrderViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OtherOrderViewModel(otherOrderRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}