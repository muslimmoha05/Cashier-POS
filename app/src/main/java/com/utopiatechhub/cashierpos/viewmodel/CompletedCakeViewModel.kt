package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.CompletedCake
import com.utopiatechhub.cashierpos.repository.CompletedCakeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompletedCakeViewModel(private val repository: CompletedCakeRepository) : ViewModel() {

    private val _completedCakes = MutableStateFlow<List<CompletedCake>>(emptyList())
    val completedCakes: StateFlow<List<CompletedCake>> = _completedCakes.asStateFlow()

    private val _cakeCounts = MutableLiveData<Map<String, Int>>()
    val cakeCounts: LiveData<Map<String, Int>> = _cakeCounts

    private val _totalCakeCount = MutableLiveData<Int?>()
    val totalCakeCount: LiveData<Int?> = _totalCakeCount

    private val _totalCakePrice = MutableLiveData<Double>()
    val totalCakePrice: LiveData<Double> = _totalCakePrice

    fun fetchTotalCakeSales() {
        viewModelScope.launch {
            val totalSales = repository.getTotalCakePrice()
            _totalCakePrice.value = totalSales
        }
    }

    fun fetchCakeCounts() {
        viewModelScope.launch {
            val counts = mapOf(
                "ሶፍት ኬክ" to repository.getCakeCount("ሶፍት ኬክ"),
                "ደረቅ ኬክ" to repository.getCakeCount("ደረቅ ኬክ"),
                "ክሬም ኬክ" to repository.getCakeCount("ክሬም ኬክ"),
                "ቦምቦሊኖ" to repository.getCakeCount("ቦምቦሊኖ"),
                "ዶናት" to repository.getCakeCount("ዶናት")
            )
            _cakeCounts.value = counts

            val total = counts.values.sum()
            _totalCakeCount.value = total
        }
    }

    fun insert(completedCake: CompletedCake) = viewModelScope.launch {
        repository.insert(completedCake)
    }

    fun fetchCompletedCakes() {
        viewModelScope.launch {
            repository.allCompletedCakes.collect {
                _completedCakes.value = it
                fetchCakeCounts()
                fetchTotalCakeSales()
            }
        }
    }

    fun deleteCompletedCakeOrder(completedCake: CompletedCake) {
        viewModelScope.launch {
            repository.delete(completedCake)
        }
    }

    fun clearCompletedCakes() {
        viewModelScope.launch {
            repository.clearCompletedCakes()
        }
    }
}