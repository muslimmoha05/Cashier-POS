package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.ManagerRepository
import com.utopiatechhub.cashierpos.viewmodel.ManagerViewModel

class ManagerViewModelFactory(private val repository: ManagerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManagerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ManagerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}