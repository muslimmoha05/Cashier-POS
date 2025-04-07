package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.CakeRepository
import com.utopiatechhub.cashierpos.viewmodel.CakeViewModel

class CakeViewModelFactory(private val repository: CakeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CakeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CakeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}