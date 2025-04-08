package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.CompletedHotDrink
import com.utopiatechhub.cashierpos.repository.CompletedHotDrinkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompletedHotDrinkViewModel(private val repository: CompletedHotDrinkRepository) : ViewModel() {

    private val _completedHotDrinks = MutableStateFlow<List<CompletedHotDrink>>(emptyList())
    val completedHotDrinks: StateFlow<List<CompletedHotDrink>> = _completedHotDrinks

    private val _hotDrinkCounts = MutableLiveData<Map<String, Int>>()
    val hotDrinkCounts: MutableLiveData<Map<String, Int>> = _hotDrinkCounts

    private val _totalHotDrinkCount = MutableLiveData<Int?>()
    val totalHotDrinkCount:LiveData<Int?> = _totalHotDrinkCount

    private val _totalHotDrinkPrice = MutableLiveData<Double>()
    val totalHotDrinkPrice: LiveData<Double> = _totalHotDrinkPrice

    fun insert(completedHotDrink: CompletedHotDrink) {
        viewModelScope.launch {
            repository.insert(completedHotDrink)
        }
    }

    fun fetchCompletedHotDrinks() {
        viewModelScope.launch {
            repository.getAllCompletedHotDrinks().collect {
                _completedHotDrinks.value = it
                fetchHotDrinkCounts()
                fetchTotalHotDrinkSales()
            }
        }
    }

    fun deleteCompletedHotDrink(completedHotDrink: CompletedHotDrink) {
        viewModelScope.launch {
            repository.deleteCompletedHotDrink(completedHotDrink)
        }
    }

    fun clearCompletedHotDrinks() {
        viewModelScope.launch {
            repository.clearCompletedHotDrinks()
        }
    }

    fun fetchTotalHotDrinkSales() {
        viewModelScope.launch {
            val totalSales = repository.getTotalHotDrinkPrice()
            _totalHotDrinkPrice.value = totalSales
        }
    }

    fun fetchHotDrinkCounts() {
        viewModelScope.launch {
            val counts = mapOf(
                "ሻይ" to repository.getHotDrinkCount("ሻይ"),
                "ሎሚ ሻይ" to repository.getHotDrinkCount("ሎሚ ሻይ"),
                "ቡና" to repository.getHotDrinkCount("ቡና"),
                "ቡና ስፕሪስ" to repository.getHotDrinkCount("ቡና ስፕሪስ"),
                "ስቲም ቡና" to repository.getHotDrinkCount("ስቲም ቡና"),
                "ማኪያቶ" to repository.getHotDrinkCount("ማኪያቶ"),
                "ወተት" to repository.getHotDrinkCount("ወተት"),
                "ፈልቶ የቀዘቀዘ ወተት" to repository.getHotDrinkCount("ፈልቶ የቀዘቀዘ ወተት"),
                "ለውዝ ሻይ" to repository.getHotDrinkCount("ለውዝ ሻይ"),
                "ለውዝ በወተት" to repository.getHotDrinkCount("ለውዝ በወተት"),
                "ለውዝ በቀሽር" to repository.getHotDrinkCount("ለውዝ በቀሽር"),
                "ወተት በቀሽር" to repository.getHotDrinkCount("ወተት በቀሽር"),
                "ካፕችኖ" to repository.getHotDrinkCount("ካፕችኖ"),
                "ማህበራዊ" to repository.getHotDrinkCount("ማህበራዊ"),
                "ቀሽር" to repository.getHotDrinkCount("ቀሽር"),
                "ጦስኝ ሻይ" to repository.getHotDrinkCount("ጦስኝ ሻይ"),
                "ብርቱካን ሻይ" to repository.getHotDrinkCount("ብርቱካን ሻይ"),
                "አናናስ ሻይ" to repository.getHotDrinkCount("አናናስ ሻይ"),
                "ማንጎ ሻይ" to repository.getHotDrinkCount("ማንጎ ሻይ"),
                "የጾም ማኪያቶ" to repository.getHotDrinkCount("የጾም ማኪያቶ")
            )
            _hotDrinkCounts.value = counts

            val total = counts.values.sum()
            _totalHotDrinkCount.value = total
        }
    }
}