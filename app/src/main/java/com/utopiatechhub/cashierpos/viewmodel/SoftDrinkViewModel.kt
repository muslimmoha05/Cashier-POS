package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.SoftDrink
import com.utopiatechhub.cashierpos.repository.SoftDrinkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SoftDrinkViewModel(private val repository: SoftDrinkRepository) : ViewModel() {

    private val _allSoftDrinks = MutableStateFlow<List<SoftDrink>>(emptyList())
    val allSoftDrinks: MutableStateFlow<List<SoftDrink>> = _allSoftDrinks

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: MutableStateFlow<String?> = _errorMessage

    init {
        fetchAllSoftDrinks()
    }

    fun fetchAllSoftDrinks() {
        viewModelScope.launch {
            try {
                repository.allSoftDrinks.collect { softDrinks ->
                    _allSoftDrinks.value = softDrinks
                    _errorMessage.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load soft drinks: ${e.message}"
            } finally {
                _errorMessage.value = null
            }
        }
    }

    fun insertSoftDrink(softDrink: SoftDrink) {
        viewModelScope.launch {
            repository.insertSoftDrink(softDrink)
        }
    }

    fun updateSoftDrink(softDrink: SoftDrink) {
        viewModelScope.launch {
            repository.updateSoftDrink(softDrink)
        }
    }

    fun deleteSoftDrink(softDrink: SoftDrink) {
        viewModelScope.launch {
            repository.deleteSoftDrink(softDrink)
        }
    }

    fun addSoftDrinksManually() {
        viewModelScope.launch {
            val softDrinks = listOf(
                SoftDrink(softDrinkName = "ግማሽ ውሃ", softDrinkPrice = 20.0),
                SoftDrink(softDrinkName = "ሩብ ውሃ", softDrinkPrice = 15.0),
                SoftDrink(softDrinkName = "1 ሊትር ውሃ", softDrinkPrice = 30.0),
                SoftDrink(softDrinkName = "2 ሊትር ውሃ", softDrinkPrice = 40.0),
                SoftDrink(softDrinkName = "ሚሪንዳ", softDrinkPrice = 40.0),
                SoftDrink(softDrinkName = "ኮካ", softDrinkPrice = 40.0),
                SoftDrink(softDrinkName = "ፋንታ አናናስ", softDrinkPrice = 40.0),
                SoftDrink(softDrinkName = "ፋንታ ኦሬንጅ", softDrinkPrice = 40.0),
                SoftDrink(softDrinkName = "ፔፕሲ", softDrinkPrice = 40.0),
                SoftDrink(softDrinkName = "ስፕራይት", softDrinkPrice = 40.0),
                SoftDrink(softDrinkName = "ኩል", softDrinkPrice = 30.0),
                SoftDrink(softDrinkName = "አምቦ", softDrinkPrice = 30.0),
            )
            repository.insertSoftDrinksManually(softDrinks)
        }
    }
}