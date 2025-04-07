package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.PackedFoodRepository
import com.utopiatechhub.cashierpos.viewmodel.PackedFoodViewModel

class PackedFoodViewModelFactory(private val repository: PackedFoodRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PackedFoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PackedFoodViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}