package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.Cake
import com.utopiatechhub.cashierpos.repository.CakeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CakeViewModel(private val repository: CakeRepository) : ViewModel() {

    private val _allCakes = MutableStateFlow<List<Cake>>(emptyList())
    val allCakes: MutableStateFlow<List<Cake>> = _allCakes

    private val _isLoading = MutableStateFlow(true)
    val isLoading: MutableStateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: MutableStateFlow<String?> = _errorMessage

    init {
        fetchAllCakes()
    }

    fun fetchAllCakes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.allCakes.collect { cakes ->
                    _allCakes.value = cakes
                    _errorMessage.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load cakes: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun insertCake(cake: Cake) {
        viewModelScope.launch {
            repository.insertCake(cake)
        }
    }

    fun updateCake(cake: Cake) {
        viewModelScope.launch {
            repository.updateCake(cake)
        }
    }

    fun deleteCake(cake: Cake) {
        viewModelScope.launch {
            repository.deleteCake(cake)
        }
    }

    fun deleteAllCakes() {
        viewModelScope.launch {
            repository.deleteAllCakes()
        }
    }

    fun addCakeManually() {
        viewModelScope.launch {
            val items = listOf(
                Cake(cakeName = "ዶናት", cakePrice = 40.00),
                Cake(cakeName = "ቦምቦሊኖ", cakePrice = 30.00),
                Cake(cakeName = "ክሬም ኬክ", cakePrice = 60.00),
                Cake(cakeName = "ሶፍት ኬክ", cakePrice = 40.00),
                Cake(cakeName = "ደረቅ ኬክ", cakePrice = 40.00),
            )
            repository.addCakeManually(items)
        }
    }
}