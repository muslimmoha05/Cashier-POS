package com.utopiatechhub.cashierpos.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.viewmodel.OrderSenderViewModel

class OrderSenderViewModelFactory(private val application: Application ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderSenderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderSenderViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}