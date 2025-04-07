package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.Manager
import com.utopiatechhub.cashierpos.repository.ManagerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ManagerViewModel(private val repository: ManagerRepository) : ViewModel() {

    val allManagers: Flow<List<Manager>> = repository.getAllManagers()

    fun insert(manager: Manager) {
        viewModelScope.launch {
            repository.insert(manager)
        }
    }

    fun delete(manager: Manager) {
        viewModelScope.launch {
            repository.delete(manager)
        }
    }
}