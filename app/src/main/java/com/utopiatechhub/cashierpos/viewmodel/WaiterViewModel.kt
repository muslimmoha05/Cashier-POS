package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.authentication.TenantDataStore
import com.utopiatechhub.cashierpos.data.Waiter
import com.utopiatechhub.cashierpos.repository.WaiterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WaiterViewModel(
    private val repository: WaiterRepository,
    private val tenantDataStore: TenantDataStore
) : ViewModel() {

    private val _tenantId = MutableStateFlow<Long?>(null)
    val tenantId: StateFlow<Long?> = _tenantId.asStateFlow()

    val allWaiters: Flow<List<Waiter>> = repository.getAllWaiters()

    init {
        viewModelScope.launch {
            tenantDataStore.tenantIdFlow.collect { id ->
                _tenantId.value = id
            }
        }
    }

    fun insert(waiter: Waiter) {
        viewModelScope.launch {
            repository.insert(waiter)
        }
    }

    fun delete(waiter: Waiter) {
        viewModelScope.launch {
            repository.delete(waiter)
        }
    }
}