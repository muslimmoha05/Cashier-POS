package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.Bread
import com.utopiatechhub.cashierpos.repository.BreadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BreadViewModel(private val repository: BreadRepository) : ViewModel() {

    private val _allBreads = MutableStateFlow<List<Bread>>(emptyList())
    val allBreads: MutableStateFlow<List<Bread>> = _allBreads

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: MutableStateFlow<String?> = _errorMessage

    init {
        fetchAllBreads()
    }

    fun fetchAllBreads() {
        viewModelScope.launch {
            try {
                repository.allBreads.collect { breads ->
                    _allBreads.value = breads
                    _errorMessage.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load breads: ${e.message}"
            } finally {
                _errorMessage.value = null
            }
        }
    }

    fun insertBread(bread: Bread) {
        viewModelScope.launch {
            repository.insertBread(bread)
        }
    }

    fun updateBread(bread: Bread) {
        viewModelScope.launch {
            repository.updateBread(bread)
        }
    }

    fun deleteBread(bread: Bread) {
        viewModelScope.launch {
            repository.deleteBread(bread)
        }
    }

    fun deleteAllBreads() {
        viewModelScope.launch {
            repository.deleteAllBreads()
        }
    }

    fun addBreadsManually() {
        viewModelScope.launch {
            val breads = listOf(
                Bread(breadName = "አሳንቡሳ", breadPrice = 30.0),
                Bread(breadName = "ጭማሪ(10)", breadPrice = 10.0),
                Bread(breadName = "ባለ 15", breadPrice = 15.0),
                Bread(breadName = "ባለ 20", breadPrice = 20.0),
                Bread(breadName = "ባለ 25", breadPrice = 25.0),
                Bread(breadName = "ድፎ(30)", breadPrice = 30.0),
                Bread(breadName = "ድፎ(40)", breadPrice = 40.0),
                Bread(breadName = "የበርገር(40)", breadPrice = 40.0),
                Bread(breadName = "ካሬ", breadPrice = 50.0),
                Bread(breadName = "ድፎ(60)", breadPrice = 60.0),
                Bread(breadName = "የገብስ", breadPrice = 20.0),
            )
            repository.insertBreadsManually(breads)
        }
    }
}