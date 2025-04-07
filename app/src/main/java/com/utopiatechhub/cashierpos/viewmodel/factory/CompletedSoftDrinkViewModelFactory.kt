package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.CompletedSoftDrinkRepository
import com.utopiatechhub.cashierpos.viewmodel.CompletedSoftDrinkViewModel

class CompletedSoftDrinkViewModelFactory(
    private val repository: CompletedSoftDrinkRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompletedSoftDrinkViewModel::class.java)) {
            return CompletedSoftDrinkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

