package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.CompletedPackedFoodRepository
import com.utopiatechhub.cashierpos.viewmodel.CompletedPackedFoodViewModel

class CompletedPackedFoodViewModelFactory(private val repository: CompletedPackedFoodRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompletedPackedFoodViewModel::class.java)) {
            return CompletedPackedFoodViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}