package com.utopiatechhub.cashierpos.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.utopiatechhub.cashierpos.authentication.TenantDataStore
import com.utopiatechhub.cashierpos.repository.WaiterRepository
import com.utopiatechhub.cashierpos.viewmodel.WaiterViewModel

class WaiterViewModelFactory(
    private val repository: WaiterRepository,
    private val tenantDataStore: TenantDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WaiterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WaiterViewModel(repository, tenantDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
