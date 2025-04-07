package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.Juice
import com.utopiatechhub.cashierpos.repository.JuiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class JuiceViewModel(private val repository: JuiceRepository) : ViewModel() {

    private val _allJuices = MutableStateFlow<List<Juice>>(emptyList())
    val allJuices: MutableStateFlow<List<Juice>> = _allJuices

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: MutableStateFlow<String?> = _errorMessage

    init {
        fetchAllJuices()
    }

    fun fetchAllJuices() {
        viewModelScope.launch {
            try {
                repository.allJuices.collect { juices ->
                    _allJuices.value = juices
                    _errorMessage.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load juices: ${e.message}"
            } finally {
                _errorMessage.value = null
            }
        }
    }

    fun insertJuice(juice: Juice) {
        viewModelScope.launch {
            repository.insertJuice(juice)
        }
    }

    fun updateJuice(juice: Juice) {
        viewModelScope.launch {
            repository.updateJuice(juice)
        }
    }

    fun deleteJuice(juice: Juice) {
        viewModelScope.launch {
            repository.deleteJuice(juice)
        }
    }

    fun addJuicesManually() {
        viewModelScope.launch {
            val juices = listOf(
                Juice(juiceName = "አቮካዶ", juicePrice = 120.0),
                Juice(juiceName = "ማንጎ", juicePrice = 120.0),
                Juice(juiceName = "ስፕሪስ", juicePrice = 120.0),
                Juice(juiceName = "ፓፓያ", juicePrice = 120.0),
                Juice(juiceName = "አናናስ", juicePrice = 120.0)
            )
            repository.insertJuicesManually(juices)
        }
    }
}