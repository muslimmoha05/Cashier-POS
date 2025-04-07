package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.CompletedJuiceRepository
import com.utopiatechhub.cashierpos.viewmodel.CompletedJuiceViewModel

class CompletedJuiceViewModelFactory(
    private val repository: CompletedJuiceRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompletedJuiceViewModel::class.java)) {
            return CompletedJuiceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
