package com.utopiatechhub.cashierpos.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.viewmodel.OrderReceiverViewModel

class OrderReceiverViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderReceiverViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderReceiverViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}