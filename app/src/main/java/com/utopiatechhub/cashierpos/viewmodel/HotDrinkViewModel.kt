package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.HotDrink
import com.utopiatechhub.cashierpos.repository.HotDrinkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HotDrinkViewModel(private val repository: HotDrinkRepository) : ViewModel() {

    private val _allHotDrinks = MutableStateFlow<List<HotDrink>>(emptyList())
    val allHotDrinks: StateFlow<List<HotDrink>> = _allHotDrinks.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        fetchAllHotDrinks()
    }

    fun fetchAllHotDrinks() {
        viewModelScope.launch {
            try {
                repository.allHotDrinks.collect { hotDrinks ->
                    _allHotDrinks.value = hotDrinks
                    _errorMessage.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load hot drinks: ${e.message}"
            } finally {
                _errorMessage.value = null
            }
        }
    }

    fun insertHotDrink(hotDrink: HotDrink) {
        viewModelScope.launch {
            repository.insertHotDrink(hotDrink)
        }
    }

    fun updateHotDrink(hotDrink: HotDrink) {
        viewModelScope.launch {
            repository.updateHotDrink(hotDrink)
        }
    }

    fun deleteHotDrink(hotDrink: HotDrink) {
        viewModelScope.launch {
            repository.deleteHotDrink(hotDrink)
        }
    }

    fun deleteAllHotDrinks() {
        viewModelScope.launch {
            repository.deleteAllHotDrinks()
        }
    }

    fun addHotDrinksManually() {
        viewModelScope.launch {
            val hotDrinks = listOf(
                HotDrink(hotDrinkName = "ሻይ", hotDrinkPrice = 20.0),
                HotDrink(hotDrinkName = "ሎሚ ሻይ", hotDrinkPrice = 25.0),
                HotDrink(hotDrinkName = "ቡና", hotDrinkPrice = 40.0),
                HotDrink(hotDrinkName = "ቡና ስፕሪስ", hotDrinkPrice = 40.0),
                HotDrink(hotDrinkName = "ስቲም ቡና", hotDrinkPrice = 30.0),
                HotDrink(hotDrinkName = "ማኪያቶ", hotDrinkPrice = 40.0),
                HotDrink(hotDrinkName = "ወተት", hotDrinkPrice = 40.0),
                HotDrink(hotDrinkName = "ፈልቶ የቀዘቀዘ ወተት", hotDrinkPrice = 60.0),
                HotDrink(hotDrinkName = "ለውዝ ሻይ", hotDrinkPrice = 40.0),
                HotDrink(hotDrinkName = "ለውዝ በወተት", hotDrinkPrice = 40.0),
                HotDrink(hotDrinkName = "ለውዝ በቀሽር", hotDrinkPrice = 40.0),
                HotDrink(hotDrinkName = "ወተት በቀሽር", hotDrinkPrice = 40.0),
                HotDrink(hotDrinkName = "ካፕችኖ", hotDrinkPrice = 60.0),
                HotDrink(hotDrinkName = "ማህበራዊ", hotDrinkPrice = 50.0),
                HotDrink(hotDrinkName = "ቀሽር", hotDrinkPrice = 30.0),
                HotDrink(hotDrinkName = "ጦስኝ ሻይ", hotDrinkPrice = 30.0),
                HotDrink(hotDrinkName = "ብርቱካን ሻይ", hotDrinkPrice = 50.0),
                HotDrink(hotDrinkName = "አናናስ ሽይ", hotDrinkPrice = 50.0),
                HotDrink(hotDrinkName = "ማንጎ ሻይ", hotDrinkPrice = 50.0),
                HotDrink(hotDrinkName = "የጾም ማኪያቶ", hotDrinkPrice = 50.0)
            )
            repository.insertHotDrinksManually(hotDrinks)
        }
    }

}