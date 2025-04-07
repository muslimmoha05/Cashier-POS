package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.CompletedSoftDrink
import com.utopiatechhub.cashierpos.repository.CompletedSoftDrinkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompletedSoftDrinkViewModel(private val repository: CompletedSoftDrinkRepository) : ViewModel() {

    private val _completedSoftDrinks = MutableStateFlow<List<CompletedSoftDrink>>(emptyList())
    val completedSoftDrinks: StateFlow<List<CompletedSoftDrink>> = _completedSoftDrinks.asStateFlow()

    private val _softDrinkCounts = MutableLiveData<Map<String, Int>>()
    val softDrinkCounts: MutableLiveData<Map<String, Int>> = _softDrinkCounts

    private val _totalSoftDrinkCount = MutableLiveData<Int?>()
    val totalSoftDrinkCount:LiveData<Int?> = _totalSoftDrinkCount

    private val _totalSoftDrinkPrice = MutableLiveData<Double>()
    val totalSoftDrinkPrice: LiveData<Double> = _totalSoftDrinkPrice

    fun insert(completedSoftDrink: CompletedSoftDrink) {
        viewModelScope.launch {
            repository.insert(completedSoftDrink)
        }
    }

    fun fetchCompletedSoftDrinks() {
        viewModelScope.launch {
            repository.getAllCompletedSoftDrinks().collect {
                _completedSoftDrinks.value = it
                fetchSoftDrinkCounts()
                fetchTotalSoftDrinkSales()
            }
        }
    }

    fun deleteCompletedSoftDrinks(completedSoftDrink: CompletedSoftDrink) {
        viewModelScope.launch {
            repository.delete(completedSoftDrink)
        }
    }

    fun clearCompletedSoftDrinks() {
        viewModelScope.launch {
            repository.clearCompletedSoftDrinks()
        }
    }

    fun fetchTotalSoftDrinkSales() {
        viewModelScope.launch {
            val totalSales = repository.getTotalSoftDrinkPrice()
            _totalSoftDrinkPrice.value = totalSales
        }
    }

    fun fetchSoftDrinkCounts() {
        viewModelScope.launch {
            val counts = mapOf(
                "ግማሽ ውሃ" to repository.getSoftDrinkCount("ግማሽ ውሃ"),
                "ሩብ ውሃ" to repository.getSoftDrinkCount("ሩብ ውሃ"),
                "1 ሊትር ውሃ" to repository.getSoftDrinkCount("1 ሊትር ውሃ"),
                "2 ሊትር ውሃ" to repository.getSoftDrinkCount("2 ሊትር ውሃ"),
                "ሚሪንዳ" to repository.getSoftDrinkCount("ሚሪንዳ"),
                "ኮካ" to repository.getSoftDrinkCount("ኮካ"),
                "ፋንታ አናናስ" to repository.getSoftDrinkCount("ፋንታ አናናስ"),
                "ፋንታ ኦሬንጅ" to repository.getSoftDrinkCount("ፋንታ ኦሬንጅ"),
                "ፔፕሲ" to repository.getSoftDrinkCount("ፔፕሲ"),
                "ስፕራይት" to repository.getSoftDrinkCount("ስፕራይት"),
                "ኩል" to repository.getSoftDrinkCount("ኩል"),
                "አምቦ" to repository.getSoftDrinkCount("አምቦ")
            )
            _softDrinkCounts.value = counts

            val total = counts.values.sum()
            _totalSoftDrinkCount.value = total
        }
    }
}