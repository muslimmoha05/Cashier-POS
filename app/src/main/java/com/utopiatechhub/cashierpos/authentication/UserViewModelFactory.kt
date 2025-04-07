package com.utopiatechhub.cashierpos.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserViewModelFactory(private val repository: UserRepository, private val tenantDataStore: TenantDataStore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository, tenantDataStore) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}