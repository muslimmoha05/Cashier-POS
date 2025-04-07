package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.CompletedJuice
import com.utopiatechhub.cashierpos.repository.CompletedJuiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CompletedJuiceViewModel(private val repository: CompletedJuiceRepository) : ViewModel() {

    private val _completedJuices = MutableStateFlow<List<CompletedJuice>>(emptyList())
    val completedJuices: MutableStateFlow<List<CompletedJuice>> = _completedJuices

    private val _juiceCounts = MutableLiveData<Map<String, Int>>()
    val juiceCounts: LiveData<Map<String, Int>> = _juiceCounts

    private val _totalJuiceCount = MutableLiveData<Int?>()
    val totalJuiceCount: LiveData<Int?> = _totalJuiceCount

    private val _totalJuicePrice = MutableLiveData<Double>()
    val totalJuicePrice: LiveData<Double> = _totalJuicePrice

    fun insert(completedJuice: CompletedJuice) {
        viewModelScope.launch {
            repository.insert(completedJuice)
        }
    }

    fun deleteCompletedJuice(completedJuice: CompletedJuice) {
        viewModelScope.launch {
            repository.deleteCompletedJuice(completedJuice)
        }
    }

    fun clearCompletedJuices() {
        viewModelScope.launch {
            repository.clearCompletedJuices()
        }
    }

    fun fetchCompletedJuices() {
        viewModelScope.launch {
            repository.getAllCompletedJuices().collect {
                _completedJuices.value = it
                fetchJuiceCounts()
                fetchTotalJuiceSales()
            }
        }
    }

    fun fetchTotalJuiceSales() {
        viewModelScope.launch {
            val totalSales = repository.getTotalJuicePrice()
            _totalJuicePrice.value = totalSales
        }
    }

    fun fetchJuiceCounts() {
        viewModelScope.launch {
            val counts = mapOf(
                "አቮካዶ" to repository.getJuiceCount("አቮካዶ"),
                "ማንጎ" to repository.getJuiceCount("ማንጎ"),
                "ስፕሪስ" to repository.getJuiceCount("ስፕሪስ"),
                "አናናስ" to repository.getJuiceCount("አናናስ"),
                "ፓፓያ" to repository.getJuiceCount("ፓፓያ")
            )
            _juiceCounts.value = counts

            val total = counts.values.sum()
            _totalJuiceCount.value = total
        }
    }
}