package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.CompletedPackedFood
import com.utopiatechhub.cashierpos.repository.CompletedPackedFoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CompletedPackedFoodViewModel(private val completedPackedFoodRepository: CompletedPackedFoodRepository) : ViewModel() {

    private val _completedPackedFoods = MutableStateFlow<List<CompletedPackedFood>>(emptyList())
    val completedPackedFoods: MutableStateFlow<List<CompletedPackedFood>> = _completedPackedFoods

    private val _packedFoodCounts = MutableLiveData<Map<String, Int>>()
    val packedFoodCounts: MutableLiveData<Map<String, Int>> = _packedFoodCounts

    private val _totalPackedFoodCount = MutableLiveData<Int?>()
    val totalPackedFoodCount: MutableLiveData<Int?> = _totalPackedFoodCount

    private val _totalPackedFoodPrice = MutableLiveData<Double?>()
    val totalPackedFoodPrice: MutableLiveData<Double?> = _totalPackedFoodPrice

    private fun fetchTotalPackedFoodSales() {
        viewModelScope.launch {
            val totalSales = completedPackedFoodRepository.getTotalCompletedPackedFoodPrice()
            _totalPackedFoodPrice.value = totalSales
        }
    }

    fun fetchPackedFoodCounts() {
        viewModelScope.launch {
            val packedFoodCounts = mapOf(
                "ሚኒ ዶናት" to completedPackedFoodRepository.getCompletedPackedFoodCount("ሚኒ ዶናት"),
                "ባቅላባ" to completedPackedFoodRepository.getCompletedPackedFoodCount("ባቅላባ"),
                "ደረቅ(የታሸገ) ኬክ" to completedPackedFoodRepository.getCompletedPackedFoodCount("ደረቅ(የታሸገ) ኬክ"),
                "ቂጣ" to completedPackedFoodRepository.getCompletedPackedFoodCount("ቂጣ"),
                "ኩኪስ(ግማሽ)" to completedPackedFoodRepository.getCompletedPackedFoodCount("ኩኪስ(ግማሽ)"),
                "ኩኪስ(ሩብ)" to completedPackedFoodRepository.getCompletedPackedFoodCount("ኩኪስ(ሩብ)"),
                "ቴምር(ሩብ)" to completedPackedFoodRepository.getCompletedPackedFoodCount("ቴምር(ሩብ)"),
                "ዓሳ(ኪሎ)" to completedPackedFoodRepository.getCompletedPackedFoodCount("ዓሳ(ኪሎ)")
            )
            _packedFoodCounts.value = packedFoodCounts

            val total = packedFoodCounts.values.sum()
            _totalPackedFoodCount.value = total
        }
    }

    fun insert(completedPackedFood: CompletedPackedFood) {
        viewModelScope.launch {
            completedPackedFoodRepository.insert(completedPackedFood)
        }
    }

    fun delete(completedPackedFood: CompletedPackedFood) {
        viewModelScope.launch {
            completedPackedFoodRepository.delete(completedPackedFood)
        }
    }

    fun clearAllCompletedPackedFoods() {
        viewModelScope.launch {
            completedPackedFoodRepository.clearAllCompletedPackedFoods()
        }
    }

    fun fetchAllCompletedPackedFoods() {
        viewModelScope.launch {
            completedPackedFoodRepository.getAllCompletedPackedFoods().collect {
                _completedPackedFoods.value = it
                fetchPackedFoodCounts()
                fetchTotalPackedFoodSales()
            }
        }
    }
}