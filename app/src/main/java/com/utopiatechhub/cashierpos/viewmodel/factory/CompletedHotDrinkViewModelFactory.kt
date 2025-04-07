package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.CompletedHotDrinkRepository
import com.utopiatechhub.cashierpos.viewmodel.CompletedHotDrinkViewModel

class CompletedHotDrinkViewModelFactory(
    private val repository: CompletedHotDrinkRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompletedHotDrinkViewModel::class.java)) {
            return CompletedHotDrinkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}