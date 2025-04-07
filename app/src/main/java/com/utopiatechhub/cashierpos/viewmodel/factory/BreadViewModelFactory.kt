package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.BreadRepository
import com.utopiatechhub.cashierpos.viewmodel.BreadViewModel

class BreadViewModelFactory(private val repository: BreadRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BreadViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BreadViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}