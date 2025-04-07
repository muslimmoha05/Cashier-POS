package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.SoftDrinkRepository
import com.utopiatechhub.cashierpos.viewmodel.SoftDrinkViewModel

class SoftDrinkViewModelFactory(private val repository: SoftDrinkRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SoftDrinkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SoftDrinkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}