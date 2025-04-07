package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.CompletedBreadRepository
import com.utopiatechhub.cashierpos.viewmodel.CompletedBreadViewModel

class CompletedBreadViewModelFactory(
    private val repository: CompletedBreadRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompletedBreadViewModel::class.java)) {
            return CompletedBreadViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}