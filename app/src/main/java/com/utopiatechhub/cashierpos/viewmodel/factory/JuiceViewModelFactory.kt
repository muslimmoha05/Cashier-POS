package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.repository.JuiceRepository
import com.utopiatechhub.cashierpos.viewmodel.JuiceViewModel

class JuiceViewModelFactory(private val repository: JuiceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JuiceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JuiceViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}