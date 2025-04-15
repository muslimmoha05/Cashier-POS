package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.PackedFood
import com.utopiatechhub.cashierpos.repository.PackedFoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PackedFoodViewModel(private val repository: PackedFoodRepository) : ViewModel() {

    private val _allPackedFoods = MutableStateFlow<List<PackedFood>>(emptyList())
    val allPackedFoods: MutableStateFlow<List<PackedFood>> = _allPackedFoods

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: MutableStateFlow<String?> = _errorMessage

    init {
        fetchAllPackedFoods()
    }

    fun fetchAllPackedFoods() {
        viewModelScope.launch {
            try {
                repository.allPackedFoods.collect { packedFoods ->
                    _allPackedFoods.value = packedFoods
                    _errorMessage.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load packed foods: ${e.message}"
            } finally {
                _errorMessage.value = null
            }
        }
    }

    fun insertPackedFood(packedFood: PackedFood) {
        viewModelScope.launch {
            repository.insertPackedFood(packedFood)
        }
    }

    fun updatePackedFood(packedFood: PackedFood) {
        viewModelScope.launch {
            repository.updatePackedFood(packedFood)
        }
    }

    fun deletePackedFood(packedFood: PackedFood) {
        viewModelScope.launch {
            repository.deletePackedFood(packedFood)
        }
    }

    fun deleteAllPackedFoods() {
        viewModelScope.launch {
            repository.deleteAllPackedFoods()
        }
    }

    fun addPackedFoodsManually() {
        viewModelScope.launch {
            val packedFoods = listOf(
                PackedFood(packedFoodName = "ሚኒ ዶናት", packedFoodPrice = 120.0),
                PackedFood(packedFoodName = "ባቅላባ", packedFoodPrice = 110.0),
                PackedFood(packedFoodName = "ደረቅ(የታሸገ) ኬክ", packedFoodPrice = 100.0),
                PackedFood(packedFoodName = "ቂጣ", packedFoodPrice = 40.0),
                PackedFood(packedFoodName = "ኩኪስ(ግማሽ)", packedFoodPrice = 250.0),
                PackedFood(packedFoodName = "ኩኪስ(ሩብ)", packedFoodPrice = 120.0),
                PackedFood(packedFoodName = "ቴምር(ሩብ)", packedFoodPrice = 120.0),
                PackedFood(packedFoodName = "ዓሳ(ኪሎ)", packedFoodPrice = 400.0)
            )
            repository.insertPackedFoodsManually(packedFoods)
        }
    }
}