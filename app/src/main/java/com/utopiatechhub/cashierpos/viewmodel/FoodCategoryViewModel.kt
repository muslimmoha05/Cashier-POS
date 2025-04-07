package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.FoodCategory
import com.utopiatechhub.cashierpos.repository.FoodCategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FoodCategoryViewModel @Inject constructor(private val repository: FoodCategoryRepository) : ViewModel() {

    private val _categories = MutableStateFlow<List<FoodCategory>>(emptyList())
    val categories: StateFlow<List<FoodCategory>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<FoodCategory?>(null)
    val selectedCategory: StateFlow<FoodCategory?> = _selectedCategory.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    init {
        fetchCategories()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.allCategories.collect { categoryList ->
                    _categories.value = categoryList
                    _errorMessage.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load categories: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCategory(foodCategory: FoodCategory) {
        viewModelScope.launch {
            repository.insertCategory(foodCategory)
        }
    }

    fun updateFoodCategory(foodCategory: FoodCategory) {
        viewModelScope.launch {
            repository.updateCategory(foodCategory)
        }
    }

    fun deleteFoodCategory(foodCategory: FoodCategory) {
        viewModelScope.launch {
            repository.deleteCategory(foodCategory)
        }
    }

    fun getCategoryById(categoryId: Long) {
        viewModelScope.launch {
            val category = repository.getCategoryById(categoryId)
            _selectedCategory.value = category
        }
    }

    fun addCategoryManually() {
        viewModelScope.launch {
            repository.addCategoryManually(
                FoodCategory(
                    categoryName = "Breakfast",
                    translatedName = "ቁርስ",
                    photoUrl = ""
                )
            )
            repository.addCategoryManually(
                FoodCategory(
                    categoryName = "Lunch and Dinner",
                    translatedName = "ምሳ እና እራት",
                    photoUrl = ""
                )
            )
            repository.addCategoryManually(
                FoodCategory(
                    categoryName = "Fasting Food",
                    translatedName = "የጾም ማግቦች",
                    photoUrl = ""
                )
            )
            repository.addCategoryManually(
                FoodCategory(
                    categoryName = "Fish",
                    translatedName = "ዓሳ",
                    photoUrl = ""
                )
            )
            repository.addCategoryManually(
                FoodCategory(
                    categoryName = "Sandwich",
                    translatedName = "ሳንዱች",
                    photoUrl = ""
                )
            )
            repository.addCategoryManually(
                FoodCategory(
                    categoryName = "Pizza",
                    translatedName = "ፒዛ",
                    photoUrl = ""
                )
            )
            repository.addCategoryManually(
                FoodCategory(
                    categoryName = "Burger",
                    translatedName = "በርገር",
                    photoUrl = ""
                )
            )
        }
    }
}