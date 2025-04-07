package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.FoodCategoryRepository
import com.utopiatechhub.cashierpos.viewmodel.FoodCategoryViewModel

class FoodCategoryViewModelFactory(private val repository: FoodCategoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodCategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodCategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}