package com.utopiatechhub.cashierpos.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
    private val tenantDataStore: TenantDataStore
) : ViewModel() {

    val tenantIdFlow = tenantDataStore.tenantIdFlow

    private suspend fun saveTenantId(tenantId: Long) {
        tenantDataStore.saveTenantId(tenantId)
    }

    fun registerUser(businessName: String, username: String, password: String, role: String, tenantId: Long, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = User(id = 0, businessName = businessName, username = username, password = password, role = role, tenantId = tenantId)
            val success = userRepository.registerUser(user)
            if (success) {
                try {
                    saveTenantId(tenantId)
                } catch (e: Exception) {
                    Log.e("UserViewModel", "Error saving Tenant ID: ${e.message}")
                }
            }
            onResult(success)
        }
    }

    fun isUsernameValid(username: String, role: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val usernameRegex = "^[a-zA-Z0-9]+$".toRegex()
            if (!username.matches(usernameRegex)) {
                onResult(false, "Username can only contain letters and numbers")
                return@launch
            }
            val isTaken = userRepository.isUsernameTaken(username, role)
            if (isTaken) {
                onResult(false, "Username is already taken")
            } else {
                onResult(true, null)
            }
        }
    }

    fun loginUser(username: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.loginUser(username, password)
            if (user != null) {
                saveTenantId(user.tenantId)
            }
            onResult(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            tenantDataStore.clearTenantId()
        }
    }
}