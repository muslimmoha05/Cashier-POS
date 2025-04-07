package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.HotDrinkRepository
import com.utopiatechhub.cashierpos.viewmodel.HotDrinkViewModel

class HotDrinkViewModelFactory(private val repository: HotDrinkRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HotDrinkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HotDrinkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}