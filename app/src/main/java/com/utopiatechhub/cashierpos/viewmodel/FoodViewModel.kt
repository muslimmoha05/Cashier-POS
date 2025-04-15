package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.Food
import com.utopiatechhub.cashierpos.data.FoodWithCategory
import com.utopiatechhub.cashierpos.repository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FoodViewModel(private val repository: FoodRepository) : ViewModel() {

    // Collect food items as StateFlow
    private val _allFoods = MutableStateFlow<List<FoodWithCategory>>(emptyList())
    val allFoods: StateFlow<List<FoodWithCategory>> = _allFoods.asStateFlow()

    // StateFlow for foods by category
    private val _categoryFoods = MutableStateFlow<List<FoodWithCategory>>(emptyList())
    val categoryFoods: StateFlow<List<FoodWithCategory>> = _categoryFoods.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error message state
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        fetchAllFoods()
    }

    private fun fetchAllFoods() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.allFoods.collect { foods ->
                    _allFoods.value = foods
                    _errorMessage.value = null // Clear any previous error
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load foods: ${e.message}"
            } finally {
                _isLoading.value = false // Set loading to false after attempting to load
            }
        }
    }

    fun insertFood(food: Food) {
        viewModelScope.launch {
            repository.insertFood(food)
        }
    }

    fun updateFood(food: Food) {
        viewModelScope.launch {
            repository.updateFood(food)
        }
    }

    fun deleteFood(food: Food) {
        viewModelScope.launch {
            repository.deleteFood(food)
        }
    }

    fun deleteAllFoods() {
        viewModelScope.launch {
            repository.deleteAllFoods()
        }
    }

    // Fetch foods by category
    fun fetchFoodsByCategory(foodCategoryId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getFoodsByCategory(foodCategoryId).collect { foods ->
                    _categoryFoods.value = foods
                    _errorMessage.value = null // Clear any previous error
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load foods for category: ${e.message}"
            } finally {
                _isLoading.value = false // Set loading to false after attempting to load
            }
        }
    }

    fun addFoodManually() {
        viewModelScope.launch {
            val items = listOf(

                Food(foodName = "ፉል ኖርማል", foodCategoryId = 1, foodPrice = 110.00),
                Food(foodName = "ፉል ስፔሻል", foodCategoryId = 1, foodPrice = 130.00),
                Food(foodName = "ፉል ሰማንዚያዳ", foodCategoryId = 1, foodPrice = 150.00),
                Food(foodName = "ፉል ሳፊ", foodCategoryId = 1, foodPrice = 150.00),
                Food(foodName = "ፉል ዘመናዊ", foodCategoryId = 1, foodPrice = 170.00),
                Food(foodName = "ፉል ሚስቶ", foodCategoryId = 1, foodPrice = 120.00),
                Food(foodName = "ፉል በርጎ", foodCategoryId = 1, foodPrice = 120.00),
                Food(foodName = "ፉል በእንቁላል", foodCategoryId = 1, foodPrice = 120.00),
                Food(foodName = "ሙሉ ስፕሪስ", foodCategoryId = 1, foodPrice = 180.00),
                Food(foodName = "ግማሽ ስፕሪስ", foodCategoryId = 1, foodPrice = 120.00),
                Food(foodName = "ግማሽ ስፕሪስ በማር", foodCategoryId = 1, foodPrice = 150.00),
                Food(foodName = "ሙሉ ስፕሪስ በማር", foodCategoryId = 1, foodPrice = 210.00),
                Food(foodName = "አዲስ ግኝት", foodCategoryId = 1, foodPrice = 170.00),
                Food(foodName = "እንቁላል ፍርፍር", foodCategoryId = 1, foodPrice = 120.00),
                Food(foodName = "እንቁላል ስልስ", foodCategoryId = 1, foodPrice = 120.00),
                Food(foodName = "እንቁላል ስፔሻል", foodCategoryId = 1, foodPrice = 150.00),
                Food(foodName = "እንቁላል ሚስቶ", foodCategoryId = 1, foodPrice = 150.00),
                Food(foodName = "እንቁላል ሃሪጋ", foodCategoryId = 1, foodPrice = 150.00),
                Food(foodName = "እንቁላል ኦምሌት", foodCategoryId = 1, foodPrice = 170.00),
                Food(foodName = "እንቁላል ቁጭቁጭ", foodCategoryId = 1, foodPrice = 170.00),
                Food(foodName = "ቂጣ ፍርፍር ስፔሻል", foodCategoryId = 1, foodPrice = 140.00),
                Food(foodName = "ፈጢራ ስፔሻል", foodCategoryId = 1, foodPrice = 140.00),
                Food(foodName = "ቂጣ ፍርፍር ኖርማል", foodCategoryId = 1, foodPrice = 120.00),
                Food(foodName = "ፈጢራ ኖርማል", foodCategoryId = 1, foodPrice = 120.00),
                Food(foodName = "ጨጨብሳ", foodCategoryId = 1, foodPrice = 150.00),
                Food(foodName = "መዐሱም", foodCategoryId = 1, foodPrice = 150.00),
                Food(foodName = "ናሽፍ ስፔሻል", foodCategoryId = 1, foodPrice = 130.00),
                Food(foodName = "ፈታህ ስፔሻል", foodCategoryId = 1, foodPrice = 130.00),
                Food(foodName = "ዳቦ ፍርፍር በእንቁላል", foodCategoryId = 1, foodPrice = 150.00),
                Food(foodName = "ጭማሪ እንቁላል", foodCategoryId = 1, foodPrice = 30.00),
                Food(foodName = "ኖርማል ዳቦ ፍርፍር", foodCategoryId = 1, foodPrice = 110.00),
                Food(foodName = "ስፔሻል ዳቦ ፍርፍር", foodCategoryId = 1, foodPrice = 150.00),
                Food(foodName = "ስፔሻል ፈጢራ በጭማሪ እንቁላል", foodCategoryId = 1, foodPrice = 170.00),
                Food(foodName = "እንቁላል ስልስ በእንጀራ", foodCategoryId = 1, foodPrice = 140.00),
                Food(foodName = "እንቁላል ፍርፍር በእንጀራ", foodCategoryId = 1, foodPrice = 140.00),
                Food(foodName = "እርጎ", foodCategoryId = 1, foodPrice = 70.00),
                Food(foodName = "ማር", foodCategoryId = 1, foodPrice = 30.00),


                Food(foodName = "ሽሮ በድስት", foodCategoryId = 2, foodPrice = 130.00),
                Food(foodName = "ሽሮ ፈሰስ", foodCategoryId = 2, foodPrice = 130.00),
                Food(foodName = "ሽሮ ፍርፍር", foodCategoryId = 2, foodPrice = 130.00),
                Food(foodName = "አትር ክክ በድስት", foodCategoryId = 2, foodPrice = 200.00),
                Food(foodName = "ምስር ክክ በድስት", foodCategoryId = 2, foodPrice = 200.00),
                Food(foodName = "ድፍን ምስር በድስት", foodCategoryId = 2, foodPrice = 200.00),
                Food(foodName = "ሽሮ በሰላጣ", foodCategoryId = 2, foodPrice = 160.00),
                Food(foodName = "ሽሮ በድስት በአትክልት", foodCategoryId = 2, foodPrice = 160.00),
                Food(foodName = "ስፔሻል ሽሮ በድስት", foodCategoryId = 2, foodPrice = 180.00),
                Food(foodName = "ስማርት ሽሮ", foodCategoryId = 2, foodPrice = 180.00),
                Food(foodName = "ተጋቢኖ", foodCategoryId = 2, foodPrice = 150.00),
                Food(foodName = "በየአይነት ኖርማል", foodCategoryId = 2, foodPrice = 150.00),
                Food(foodName = "በየአይነት ስፔሻል", foodCategoryId = 2, foodPrice = 280.00),
                Food(foodName = "ማህበራዊ", foodCategoryId = 2, foodPrice = 250.00),
                Food(foodName = "እንጀራ ፍርፍር ኖርማል", foodCategoryId = 2, foodPrice = 120.00),
                Food(foodName = "እንጀራ ፍርፍር ስፔሻል", foodCategoryId = 2, foodPrice = 200.00),
                Food(foodName = "ቲማቲም ለብለብ", foodCategoryId = 2, foodPrice = 170.00),
                Food(foodName = "ዶሮ ፋንታ", foodCategoryId = 2, foodPrice = 200.00),
                Food(foodName = "ፓስታ በስጎ", foodCategoryId = 2, foodPrice = 150.00),
                Food(foodName = "ፓስታ ባትክልት", foodCategoryId = 2, foodPrice = 180.00),
                Food(foodName = "ፓስታ በእንጀራ", foodCategoryId = 2, foodPrice = 170.00),
                Food(foodName = "መኮረኒ በስጎ", foodCategoryId = 2, foodPrice = 160.00),
                Food(foodName = "መኮረኒ በአትክልት", foodCategoryId = 2, foodPrice = 190.00),
                Food(foodName = "ሩዝ በስጎ", foodCategoryId = 2, foodPrice = 180.00),
                Food(foodName = "ሩዝ በአትክልት", foodCategoryId = 2, foodPrice = 230.00),
                Food(foodName = "አትክልት በዳቦ ኖርማል", foodCategoryId = 2, foodPrice = 100.00),
                Food(foodName = "አትክልት በዳቦ ስፔሻል", foodCategoryId = 2, foodPrice = 150.00),
                Food(foodName = "አትክልት በእንጀራ ኖርማል", foodCategoryId = 2, foodPrice = 150.00),
                Food(foodName = "አትክልት በእንጀራ ስፔሻል", foodCategoryId = 2, foodPrice = 200.00),
                Food(foodName = "ፓስታ በአትክልት በእንጀራ", foodCategoryId = 2, foodPrice = 200.00),
                Food(foodName = "ሽሮ በድስት በቅቤ", foodCategoryId = 2, foodPrice = 180.00),
                Food(foodName = "ሽሮ በድስት በዳቦ", foodCategoryId = 2, foodPrice = 130.00),
                Food(foodName = "ተጋቢኖ በዳቦ", foodCategoryId = 2, foodPrice = 150.00),
                Food(foodName = "ሙሉ እንጀራ ፍርፍር", foodCategoryId = 2, foodPrice = 150.00),
                Food(foodName = "ሙሉ እንጀራ ፍርፍር ስፔሻል", foodCategoryId = 2, foodPrice = 250.00),
                Food(foodName = "መኮረኒ በስጎ በእንጀራ", foodCategoryId = 2, foodPrice = 180.00),
                Food(foodName = "መኮረኒ በአትክልት በእንጀራ", foodCategoryId = 2, foodPrice = 210.00),
                Food(foodName = "ደረቅ እንጀራ", foodCategoryId = 2, foodPrice = 30.00),
                Food(foodName = "ግማሽ እንጀራ", foodCategoryId = 2, foodPrice = 20.00),
                Food(foodName = "ቅቤ", foodCategoryId = 2, foodPrice = 50.00),
                Food(foodName = "ሰላጣ", foodCategoryId = 2, foodPrice = 40.00),

                Food(foodName = "የጾም ፉል", foodCategoryId = 3, foodPrice = 110.00),
                Food(foodName = "የጾም ፈጢራ", foodCategoryId = 3, foodPrice = 120.00),
                Food(foodName = "የጾም ቂጣ ፍርፍር", foodCategoryId = 3, foodPrice = 120.00),
                Food(foodName = "የጾም ጨጨብሳ", foodCategoryId = 3, foodPrice = 130.00),
                Food(foodName = "የጾም መዐሱም", foodCategoryId = 3, foodPrice = 150.00),
                Food(foodName = "ሙሉ ስፕሪስ የጾም", foodCategoryId = 3, foodPrice = 180.00),
                Food(foodName = "ግማሽ ስፕሪስ የጾም", foodCategoryId = 3, foodPrice = 120.00),
                Food(foodName = "የጾም ዳቦ ፍርፍር", foodCategoryId = 3, foodPrice = 110.00),
                Food(foodName = "የጾም እንጀራ ፍርፍር", foodCategoryId = 3, foodPrice = 130.00),
                Food(foodName = "ቲማቲም ስልስ", foodCategoryId = 3, foodPrice = 120.00),
                Food(foodName = "የጾም ግማሽ ስፕሪስ በማር", foodCategoryId = 3, foodPrice = 150.00),
                Food(foodName = "የጾም ሙሉ ስፕሪስ በማር", foodCategoryId = 3, foodPrice = 210.00),
                Food(foodName = "ቲማቲም ስልስ በእንጀራ", foodCategoryId = 3, foodPrice = 140.00),
                Food(foodName = "የጾም ስፔሻል እንጀራ ፍርፍር", foodCategoryId = 3, foodPrice = 200.00),

                Food(foodName = "ዓሳ ወጥ", foodCategoryId = 4, foodPrice = 250.00),
                Food(foodName = "ዓሳ ለብለብ", foodCategoryId = 4, foodPrice = 250.00),
                Food(foodName = "ዓሳ ጎረድ", foodCategoryId = 4, foodPrice = 250.00),
                Food(foodName = "ዓሳ ጉላሽ", foodCategoryId = 4, foodPrice = 250.00),
                Food(foodName = "ዓሳ ኮተሌት", foodCategoryId = 4, foodPrice = 300.00),
                Food(foodName = "ዓሳ ሸክላ", foodCategoryId = 4, foodPrice = 350.00),
                Food(foodName = "ዓሳ ዱለት", foodCategoryId = 4, foodPrice = 250.00),
                Food(foodName = "ዓሳ ሳንዱች", foodCategoryId = 4, foodPrice = 150.00),
                Food(foodName = "ዓሳ ቋንጣ ፍርፍር", foodCategoryId = 4, foodPrice = 200.00),
                Food(foodName = "ዓሳ ክትፎ", foodCategoryId = 4, foodPrice = 300.00),

                Food(foodName = "ክለብ ሳንዱች", foodCategoryId = 5, foodPrice = 300.00),
                Food(foodName = "ስፔሻል ክለብ ሳንዱች", foodCategoryId = 5, foodPrice = 350.00),
                Food(foodName = "ቢፍ ክለብ ሳንዱች", foodCategoryId = 5, foodPrice = 300.00),
                Food(foodName = "ቱና ክለብ ሳንዱች", foodCategoryId = 5, foodPrice = 300.00),
                Food(foodName = "ቺዝ ክለብ ሳንዱች", foodCategoryId = 5, foodPrice = 330.00),
                Food(foodName = "አትክልት ሳንዱች", foodCategoryId = 5, foodPrice = 130.00),
                Food(foodName = "ቺዝ ሳንዱች", foodCategoryId = 5, foodPrice = 150.00),
                Food(foodName = "ቱና ሳንዱች", foodCategoryId = 5, foodPrice = 200.00),
                Food(foodName = "ስፔሻል ሳንዱች", foodCategoryId = 5, foodPrice = 200.00),
                Food(foodName = "እንቁላል ሳንዱች", foodCategoryId = 5, foodPrice = 150.00),
                Food(foodName = "ስፔሻል ቱና ሳንዱች", foodCategoryId = 5, foodPrice = 300.00),
                Food(foodName = "ባናና ሳንዱች", foodCategoryId = 5, foodPrice = 150.00),
                Food(foodName = "ችፕስ", foodCategoryId = 5, foodPrice = 130.00),

                Food(foodName = "ስፔሻል ፒዛ", foodCategoryId = 6, foodPrice = 400.00),
                Food(foodName = "ፍሩት ፒዛ", foodCategoryId = 6, foodPrice = 300.00),
                Food(foodName = "ቱና ፒዛ", foodCategoryId = 6, foodPrice = 380.00),
                Food(foodName = "አትክልት ፒዛ", foodCategoryId = 6, foodPrice = 250.00),
                Food(foodName = "ቺዝ ፒዛ", foodCategoryId = 6, foodPrice = 380.00),
                Food(foodName = "መሽሩም ፒዛ", foodCategoryId = 6, foodPrice = 380.00),
                Food(foodName = "ስፔሻል አትክልት ፒዛ", foodCategoryId = 6, foodPrice = 350.00),
                Food(foodName = "አትክልት በቱና ፒዛ", foodCategoryId = 6, foodPrice = 350.00),
                Food(foodName = "ቱና ዊዝ ቺዝ ፒዛ", foodCategoryId = 6, foodPrice = 400.00),
                Food(foodName = "ማርጋሪታ ፒዛ", foodCategoryId = 6, foodPrice = 380.00),
                Food(foodName = "ፎርኮርነር ፒዛ", foodCategoryId = 6, foodPrice = 400.00),
                Food(foodName = "አቢሲኒያ ፒዛ", foodCategoryId = 6, foodPrice = 350.00),
                Food(foodName = "የቤቱ ስፔሻል ፒዛ", foodCategoryId = 6, foodPrice = 450.00),
                Food(foodName = "ሚኒ ፒዛ", foodCategoryId = 6, foodPrice = 200.00),
                Food(foodName = "ላርጅ ፒዛ", foodCategoryId = 6, foodPrice = 400.00),
                Food(foodName = "ኦሬንታል ፒዛ", foodCategoryId = 6, foodPrice = 350.00),

                Food(foodName = "ኖርማል በርገር", foodCategoryId = 7, foodPrice = 300.00),
                Food(foodName = "ደብል ቺዝ በርገር", foodCategoryId = 7, foodPrice = 500.00),
                Food(foodName = "ደብል ስፔሻል ቱና በርገር", foodCategoryId = 7, foodPrice = 500.00),
                Food(foodName = "ደብል ስፔሻል በርገር", foodCategoryId = 7, foodPrice = 500.00),
                Food(foodName = "ደብል ኖርማል በርገር", foodCategoryId = 7, foodPrice = 450.00),
                Food(foodName = "ስፔሻል በርገር", foodCategoryId = 7, foodPrice = 350.00),
                Food(foodName = "ቺዝ በርገር", foodCategoryId = 7, foodPrice = 330.00),
                Food(foodName = "ኖርማል አትክልት በርገር", foodCategoryId = 7, foodPrice = 250.00),
                Food(foodName = "ስፔሻል አትክልት በርገር", foodCategoryId = 7, foodPrice = 300.00),
                Food(foodName = "ቱና በርገር", foodCategoryId = 7, foodPrice = 350.00),
                Food(foodName = "ኖርማል ዓሳ በርገር", foodCategoryId = 7, foodPrice = 300.00),
                Food(foodName = "ስፔሻል ዓሳ በርገር", foodCategoryId = 7, foodPrice = 350.00),
                Food(foodName = "ቺዝ ዓሳ በርገር", foodCategoryId = 7, foodPrice = 300.00),
                Food(foodName = "ደብል ስፔሻል ዓሳ በርገር", foodCategoryId = 7, foodPrice = 450.00),
                Food(foodName = "ደብል ኖርማል ዓሳ በርገር", foodCategoryId = 7, foodPrice = 400.00),
                Food(foodName = "ካቻፕ", foodCategoryId = 7, foodPrice = 40.00),

            )
            repository.addFoodManually(items)
        }
    }
}