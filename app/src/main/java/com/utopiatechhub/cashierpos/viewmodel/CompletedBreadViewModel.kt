package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.CompletedBread
import com.utopiatechhub.cashierpos.repository.CompletedBreadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompletedBreadViewModel(private val repository: CompletedBreadRepository) : ViewModel() {

    private val _completedBreads = MutableStateFlow<List<CompletedBread>>(emptyList())
    val completedBreads: StateFlow<List<CompletedBread>> = _completedBreads

    private val _breadCounts = MutableLiveData<Map<String, Int>>()
    val breadCounts: LiveData<Map<String, Int>> = _breadCounts

    private val _totalBreadCount = MutableLiveData<Int?>()
    val totalBreadCount: LiveData<Int?> = _totalBreadCount

    private val _totalBreadPrice = MutableLiveData<Double>()
    val totalBreadPrice: LiveData<Double> = _totalBreadPrice

    fun fetchTotalBreadSales() {
        viewModelScope.launch {
            val totalSales = repository.getTotalBreadPrice()
            _totalBreadPrice.value = totalSales
        }
    }

    fun fetchBreadCounts() {
        viewModelScope.launch {
            val counts = mapOf(
                "አሳንቡሳ" to repository.getBreadCount("አሳንቡሳ"),
                "ጭማሪ(10)" to repository.getBreadCount("ጭማሪ(10)"),
                "ባለ 15" to repository.getBreadCount("ባለ 15"),
                "ባለ 20" to repository.getBreadCount("ባለ 20"),
                "ባለ 25" to repository.getBreadCount("ባለ 25"),
                "ድፎ(30)" to repository.getBreadCount("ድፎ(30)"),
                "ድፎ(40)" to repository.getBreadCount("ድፎ(40)"),
                "የበርገር(40)" to repository.getBreadCount("የበርገር(40)"),
                "ድፎ(60)" to repository.getBreadCount("ድፎ(60)"),
                "ካሬ" to repository.getBreadCount("ካሬ"),
                "የገብስ" to repository.getBreadCount("የገብስ"),
            )
            _breadCounts.value = counts

            val total = counts.values.sum()
            _totalBreadCount.value = total
        }
    }

    fun insert(completedBread: CompletedBread) {
        viewModelScope.launch {
            repository.insert(completedBread)
        }
    }

    fun fetchCompletedBreads() {
        viewModelScope.launch {
            repository.getAllCompletedBreads().collect {
                _completedBreads.value = it
                fetchBreadCounts()
                fetchTotalBreadSales()
            }
        }
    }

    fun deleteCompletedBread(completedBread: CompletedBread) {
        viewModelScope.launch {
            repository.deleteCompletedBread(completedBread)
        }
    }

    fun clearCompletedBreads() {
        viewModelScope.launch {
            repository.clearCompletedBreads()
        }
    }
}