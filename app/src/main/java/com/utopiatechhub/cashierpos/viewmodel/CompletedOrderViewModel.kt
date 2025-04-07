package com.utopiatechhub.cashierpos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utopiatechhub.cashierpos.data.CompletedOrder
import com.utopiatechhub.cashierpos.repository.CompletedOrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CompletedOrderViewModel(private val completedOrderRepository: CompletedOrderRepository) : ViewModel() {

    private val _completedOrders = MutableStateFlow<List<CompletedOrder>>(emptyList())
    val completedOrders: StateFlow<List<CompletedOrder>> = _completedOrders.asStateFlow()

    private val _foodCounts = MutableLiveData<Map<String, Int>>()
    val foodCounts: LiveData<Map<String, Int>> get() = _foodCounts

    private val _totalCount = MutableLiveData<Int>()
    val totalCount: LiveData<Int> get() = _totalCount

    private val _categoryCounts = MutableLiveData<Map<String, Int>>()
    val categoryCounts: LiveData<Map<String, Int>> get() = _categoryCounts

    private val _totalSales = MutableLiveData<Double?>()
    val totalSalesLiveData: LiveData<Double?> get() = _totalSales

    fun insertCompletedOrder(completedOrder: CompletedOrder) {
        viewModelScope.launch {
            completedOrderRepository.insertCompletedOrder(completedOrder)
        }
    }

    fun fetchCompletedOrders() {
        viewModelScope.launch {
            completedOrderRepository.getAllCompletedOrders().collect {
                _completedOrders.value = it
            }
        }
    }

    fun deleteCompletedOrder(completedOrder: CompletedOrder) {
        viewModelScope.launch {
            completedOrderRepository.deleteCompletedOrder(completedOrder)
        }
    }

    fun clearCompletedOrders() {
        viewModelScope.launch {
            completedOrderRepository.clearCompletedOrders()
        }
    }

    fun fetchTotalFoodSales() {
        viewModelScope.launch(Dispatchers.IO) {
            val totalSales = completedOrderRepository.getTotalFoodPrice()
            withContext(Dispatchers.Main) {
                _totalSales.value = totalSales
            }
        }
    }

    private val foodCategories = mapOf(
        "እንጀራ" to listOf(
            "እንቁላል ስልስ በእንጀራ", "እንቁላል ፍርፍር በእንጀራ", "ሽሮ በድስት", "ሽሮ ፈሰስ", "ሽሮ ፍርፍር", "አተር ክክ በድስት", "ምስር ክክ በድስት",
            "ድፍን ምስር በድስት", "ሽሮ በሰላጣ", "ሽሮ በድስት በአትክልት", "ስፔሻል ሽሮ በድስት", "ስማርት ሽሮ", "ተጋቢኖ", "በየአይነት ኖርማል",
            "በየአይነት ስፔሻል", "ማህበራዊ", "እንጀራ ፍርፍር ኖርማል", "እንጀራ ፍርፍር ስፔሻል", "ቲማቲም ለብለብ", "ዶሮ ፋንታ", "ፓስታ በእንጀራ",
            "አትክልት በእንጀራ ኖርማል", "አትክልት በእንጀራ ስፔሻል", "ፓስታ በአትክልት በእንጀራ", "ሙሉ እንጀራ ፍርፍር", "ሽሮ በድስት በቅቤ",
            "ደረቅ እንጀራ", "ሙሉ እንጀራ ፍርፍር ስፔሻል", "መኮረኒ በስጎ በእንጀራ", "መኮረኒ በአትክልት በእንጀራ", "ግማሽ እንጀራ", "የጾም እንጀራ ፍርፍር",
            "የጾም ስፔሻል እንጀራ ፍርፍር", "ቲማቲም ስልስ በእንጀራ", "ዓሳ ወጥ", "ዓሳ ለብለብ", "አሳ ጎርድ", "አሳ ጉላሽ", "ዓሳ ሽክላ", "አሳ ዱለት",
            "ዓሳ ቋንጣ ፍርፍር", "ዓሳ ክትፎ",
        ),
        "ቂጣ" to listOf(
            "ስፔሻል ፈጢራ በጭማሪ እንቁላል", "መዐሱም", "ጨጨብሳ ኖርማል", "ጨጨብሳ ስፔሻል", "ፈጢራ ኖርማል", "ቂጣ ፍርፍር ኖርማል", "ፈጢራ ስፔሻል",
            "ቂጣ ፍርፍር ስፔሻል", "የጾም መዐሱም", "የጾም ጨጨብሳ", "የጾም ቂጣ ፍርፍር", "የጾም ፈጢራ", "ናሽፍ ስፔሻል", "ፈታህ ስፔሻል",
        ),
        "ፉል" to listOf(
            "ፉል ኖርማል", "ፉል ስፔሻል", "ፉል ሰማንዚያዳ", "ፉል ዘመናዊ", "ፉል ሚስቶ", "ፉል በርጎ", "ፉል በእንቁላል", "ሙሉ ስፕሪስ", "ግማሽ ስፕሪስ", "አዲስ ግኝት",
            "ግማሽ ስፕሪስ በማር", "ሙሉ ስፕሪስ በማር", "ፉል ሳፊ", "የጾም ፉል", "ሙሉ ስፕሪስ የጾም", "ግማሽ ስፕሪስ የጾም", "የጾም ግማሽ ስፕሪስ በማር", "የጾም ሙሉ ስፕሪስ በማር",
        ),
        "እንቁላል" to listOf(
            "እንቁላል ፍርፍር", "እንቁላል ስልስ", "እንቁላል ስፔሻል", "እንቁላል ሚስቶ", "እንቁላል ሃሪጋ", "እንቁላል ኦምሌት", "እንቁላል ቁጭቁጭ", "ስፔሻል ፈጢራ በጭማሪ እንቁላል",
            "እንቁላል ስልስ በእንጀራ", "እንቁላል ፍርፍር በእንጀራ",
        ),
        "ዓሳ" to listOf(
            "ዓሳ ወጥ", "ዓሳ ለብለብ", "ዓሳ ጎረድ", "ዓሳ ጉላሽ", "ዓሳ ኮተሌት", "ዓሳ ሸክላ", "ዓሳ ዱለት", "ዓሳ ሳንዱች", "ዓሳ ቋንጣ ፍርፍር",
            "ዓሳ ክትፎ",
        ),
        "በርገር" to listOf(
            "ካቻፕ", "ደብል ኖርማል ዓሳ በርገር", "ደብል ስፔሻል ዓሳ በርገር", "ቺዝ ዓሳ በርገር", "ስፔሻል ዓሳ በርገር",  "ኖርማል ዓሳ በርገር",
            "ቱና በርገር", "ስፔሻል አትክልት በርገር", "ኖርማል አትክልት በርገር", "ቺዝ በርገር", "ስፔሻል በርገር", "ደብል ኖርማል በርገር",
            "ደብል ስፔሻል በርገር", "ደብል ስፔሻል ቱና በርገር", "ኖርማል በርገር",
        ),
        "ፒዛ" to listOf(
            "ስፔሻል ፒዛ", "ፍሩት ፒዛ", "ቱና ፒዛ", "አትክልት ፒዛ", "መሽሩም ፒዛ", "ስፔሻል አትክልት ፒዛ", "አትክልት በቱና ፒዛ", "ቱና ዊዝ ቺዝ ፒዛ",
            "ማርጋሪታ ፒዛ", "ፎርኮርነር ፒዛ", "አቢሲኒያ ፒዛ", "የቤቱ ስፔሻል ፒዛ", "ሚኒ ፒዛ", "ላርጅ ፒዛ",  "ኦሬንታል ፒዛ",
        ),
        "ሳንዱች" to listOf(
            "ክለብ ሳንዱች",  "ስፔሻል ክለብ ሳንዱች", "ቢፍ ክለብ ሳንዱች", "ቱና ክለብ ሳንዱች", "ቺዝ ክለብ ሳንዱች", "አትክልት ሳንዱች", "ቺዝ ሳንዱች",
            "ቱና ሳንዱች", "ስፔሻል ሳንዱች", "ኖርማል እንቁላል ሳንዱች", "ስፔሻል ቱና ሳንዱች", "ባናና ሳንዱች", "ችፕስ",
        )
    )

    fun fetchFoodCounts() {
        viewModelScope.launch(Dispatchers.IO) {
            val counts = mapOf(

                "ፉል ኖርማል" to completedOrderRepository.getFoodCount("ፉል ኖርማል"),
                "ፉል ስፔሻል" to completedOrderRepository.getFoodCount("ፉል ስፔሻል"),
                "ፉል ሰማንዚያዳ" to completedOrderRepository.getFoodCount("ፉል ሰማንዚያዳ"),
                "ፉል ዘመናዊ" to completedOrderRepository.getFoodCount("ፉል ዘመናዊ"),
                "ፉል ሚስቶ" to completedOrderRepository.getFoodCount("ፉል ሚስቶ"),
                "ፉል በርጎ" to completedOrderRepository.getFoodCount("ፉል በርጎ"),
                "ፉል በእንቁላል" to completedOrderRepository.getFoodCount("ፉል በእንቁላል"),
                "ሙሉ ስፕሪስ" to completedOrderRepository.getFoodCount("ሙሉ ስፕሪስ"),
                "ግማሽ ስፕሪስ" to completedOrderRepository.getFoodCount("ግማሽ ስፕሪስ"),
                "አዲስ ግኝት" to completedOrderRepository.getFoodCount("አዲስ ግኝት"),
                "እንቁላል ፍርፍር" to completedOrderRepository.getFoodCount("እንቁላል ፍርፍር"),
                "እንቁላል ስልስ" to completedOrderRepository.getFoodCount("እንቁላል ስልስ"),
                "እንቁላል ስፔሻል" to completedOrderRepository.getFoodCount("እንቁላል ስፔሻል"),
                "እንቁላል ሚስቶ" to completedOrderRepository.getFoodCount("እንቁላል ሚስቶ"),
                "እንቁላል ሃሪጋ" to completedOrderRepository.getFoodCount("እንቁላል ሃሪጋ"),
                "እንቁላል ኦምሌት" to completedOrderRepository.getFoodCount("እንቁላል ኦምሌት"),
                "እንቁላል ቁጭቁጭ" to completedOrderRepository.getFoodCount("እንቁላል ቁጭቁጭ"),
                "ቂጣ ፍርፍር ስፔሻል" to completedOrderRepository.getFoodCount("ቂጣ ፍርፍር ስፔሻል"),
                "ፈጢራ ስፔሻል" to completedOrderRepository.getFoodCount("ፈጢራ ስፔሻል"),
                "ቂጣ ፍርፍር ኖርማል" to completedOrderRepository.getFoodCount("ቂጣ ፍርፍር ኖርማል"),
                "ፈጢራ ኖርማል" to completedOrderRepository.getFoodCount("ፈጢራ ኖርማል"),
                "ጨጨብሳ ስፔሻል" to completedOrderRepository.getFoodCount("ጨጨብሳ ስፔሻል"),
                "ጨጨብሳ ኖርማል" to completedOrderRepository.getFoodCount("ጨጨብሳ ኖርማል"),
                "መዐሱም" to completedOrderRepository.getFoodCount("መዐሱም"),
                "ናሽፍ ስፔሻል" to completedOrderRepository.getFoodCount("ናሽፍ ስፔሻል"),
                "ፈታህ ስፔሻል" to completedOrderRepository.getFoodCount("ፈታህ ስፔሻል"),
                "ዳቦ ፍርፍር በእንቁላል" to completedOrderRepository.getFoodCount("ዳቦ ፍርፍር በእንቁላል"),
                "ግማሽ ስፕሪስ በማር" to completedOrderRepository.getFoodCount("ግ�maሽ ስፕሪስ በማር"),
                "ሙሉ ስፕሪስ በማር" to completedOrderRepository.getFoodCount("ሙሉ ስፕሪስ በማር"),
                "ማር" to completedOrderRepository.getFoodCount("ማር"),
                "ስፔሻል ዳቦ ፍርፍር" to completedOrderRepository.getFoodCount("ስፔሻል ዳቦ ፍርፍር"),
                "ስፔሻል ፈጢራ በጭማሪ እንቁላል" to completedOrderRepository.getFoodCount("ስፔሻል ፈጢራ በጭማሪ እንቁላል"),
                "ጭማሪ እንቁላል" to completedOrderRepository.getFoodCount("ጭማሪ እንቁላል"),
                "ፉል ሳፊ" to completedOrderRepository.getFoodCount("ፉል ሳፊ"),
                "እርጎ" to completedOrderRepository.getFoodCount("እርጎ"),
                "እንቁላል ስልስ በእንጀራ" to completedOrderRepository.getFoodCount("እንቁላል ስልስ በእንጀራ"),
                "እንቁላል ፍርፍር በእንጀራ" to completedOrderRepository.getFoodCount("እንቁላል ፍርፍር በእንጀራ"),

                "ሽሮ በድስት" to completedOrderRepository.getFoodCount("ሽሮ በድስት"),
                "ሽሮ ፈሰስ" to completedOrderRepository.getFoodCount("ሽሮ ፈሰስ"),
                "ሽሮ ፍርፍር" to completedOrderRepository.getFoodCount("ሽሮ ፍርፍር"),
                "አትር ክክ በድስት" to completedOrderRepository.getFoodCount("አትር ክክ በድስት"),
                "ምስር ክክ በድስት" to completedOrderRepository.getFoodCount("ምስር ክክ በድስት"),
                "ድፍን ምስር በድስት" to completedOrderRepository.getFoodCount("ድፍን ምስር በድስት"),
                "ሽሮ በሰላጣ" to completedOrderRepository.getFoodCount("ሽሮ በሰላጣ"),
                "ሽሮ በድስት በአትክልት" to completedOrderRepository.getFoodCount("ሽሮ በድስት በአትክልት"),
                "ስፔሻል ሽሮ በድስት" to completedOrderRepository.getFoodCount("ስፔሻል ሽሮ በድስት"),
                "ስማርት ሽሮ" to completedOrderRepository.getFoodCount("ስማርት ሽሮ"),
                "ተጋቢኖ" to completedOrderRepository.getFoodCount("ተጋቢኖ"),
                "በየአይነት ኖርማል" to completedOrderRepository.getFoodCount("በየአይነት ኖርማል"),
                "በየአይነት ስፔሻል" to completedOrderRepository.getFoodCount("በየአይነት ስፔሻል"),
                "ማህበራዊ" to completedOrderRepository.getFoodCount("ማህበራዊ"),
                "እንጀራ ፍርፍር ኖርማል" to completedOrderRepository.getFoodCount("እንጀራ ፍርፍር ኖርማል"),
                "እንጀራ ፍርፍር ስፔሻል" to completedOrderRepository.getFoodCount("እንጀራ ፍርፍር ስፔሻል"),
                "ቲማቲም ለብለብ" to completedOrderRepository.getFoodCount("ቲማቲም ለብለብ"),
                "ዶሮ ፋንታ" to completedOrderRepository.getFoodCount("ዶሮ ፋንታ"),
                "ፓስታ በስጎ" to completedOrderRepository.getFoodCount("ፓስታ በስጎ"),
                "ፓስታ ባትክልት" to completedOrderRepository.getFoodCount("ፓስታ ባትክልት"),
                "ፓስታ በእንጀራ" to completedOrderRepository.getFoodCount("ፓስታ በእንጀራ"),
                "መኮረኒ በስጎ" to completedOrderRepository.getFoodCount("መኮረኒ በስጎ"),
                "መኮረኒ በአትክልት" to completedOrderRepository.getFoodCount("መኮረኒ በአትክልት"),
                "ሩዝ በስጎ" to completedOrderRepository.getFoodCount("ሩዝ በስጎ"),
                "ሩዝ በአትክልት" to completedOrderRepository.getFoodCount("ሩዝ በአትክልት"),
                "አትክልት በዳቦ ኖርማል" to completedOrderRepository.getFoodCount("አትክልት በዳቦ ኖርማል"),
                "አትክልት በዳቦ ስፔሻል" to completedOrderRepository.getFoodCount("አትክልት በዳቦ ስፔሻል"),
                "አትክልት በእንጀራ ኖርማል" to completedOrderRepository.getFoodCount("አትክልት በእንጀራ ኖርማል"),
                "አትክልት በእንጀራ ስፔሻል" to completedOrderRepository.getFoodCount("አትክልት በእንጀራ ስፔሻል"),
                "ፓስታ በአትክልት በእንጀራ" to completedOrderRepository.getFoodCount("ፓስታ በአትክልት በእንጀራ"),
                "ሙሉ እንጀራ ፍርፍር" to completedOrderRepository.getFoodCount("ሙሉ እንጀራ ፍርፍር"),
                "ቅቤ" to completedOrderRepository.getFoodCount("ቅቤ"),
                "ደረቅ እንጀራ" to completedOrderRepository.getFoodCount("ደረቅ እንጀራ"),
                "ሽሮ በድስት በቅቤ" to completedOrderRepository.getFoodCount("ሽሮ በድስት በቅቤ"),
                "ሽሮ በድስት በዳቦ" to completedOrderRepository.getFoodCount("ሽሮ በድስት በዳቦ"),
                "ተጋቢኖ በዳቦ" to completedOrderRepository.getFoodCount("ተጋቢኖ በዳቦ"),
                "ግማሽ እንጀራ" to completedOrderRepository.getFoodCount("ግማሽ እንጀራ"),
                "ሙሉ እንጀራ ፍርፍር ስፔሻል" to completedOrderRepository.getFoodCount("ሙሉ እንጀራ ፍርፍር ስፔሻል"),
                "መኮረኒ በስጎ በእንጀራ" to completedOrderRepository.getFoodCount("መኮረኒ በስጎ በእንጀራ"),
                "መኮረኒ በአትክልት በእንጀራ" to completedOrderRepository.getFoodCount("መኮረኒ በአትክልት በእንጀራ"),
                "ሰላጣ" to completedOrderRepository.getFoodCount("ሰላጣ"),

                "የጾም ፉል" to completedOrderRepository.getFoodCount("የጾም ፉል"),
                "የጾም ፈጢራ" to completedOrderRepository.getFoodCount("የጾም ፈጢራ"),
                "የጾም ቂጣ ፍርፍር" to completedOrderRepository.getFoodCount("የጾም ቂጣ ፍርፍር"),
                "የጾም ጨጨብሳ" to completedOrderRepository.getFoodCount("የጾም ጨጨብሳ"),
                "የጾም መዐሱም" to completedOrderRepository.getFoodCount("የጾም መዐሱም"),
                "ሙሉ ስፕሪስ የጾም" to completedOrderRepository.getFoodCount("ሙሉ ስፕሪስ የጾም"),
                "ግማሽ ስፕሪስ የጾም" to completedOrderRepository.getFoodCount("ግማሽ ስፕሪስ የጾም"),
                "የጾም ዳቦ ፍርፍር" to completedOrderRepository.getFoodCount("የጾም ዳቦ ፍርፍር"),
                "የጾም እንጀራ ፍርፍር" to completedOrderRepository.getFoodCount("የጾም እንጀራ ፍርፍር"),
                "ቲማቲም ስልስ" to completedOrderRepository.getFoodCount("ቲማቲም ስልስ"),
                "የጾም ግማሽ ስፕሪስ በማር" to completedOrderRepository.getFoodCount("የጾም ግማሽ ስፕሪስ በማር"),
                "የጾም ሙሉ ስፕሪስ በማር" to completedOrderRepository.getFoodCount("የጾም ሙሉ ስፕሪስ በማር"),
                "ቲማቲም ስልስ በእንጀራ" to completedOrderRepository.getFoodCount("ቲማቲም ስልስ በእንጀራ"),
                "የጾም ስፔሻል እንጀራ ፍርፍር" to completedOrderRepository.getFoodCount("የጾም ስፔሻል እንጀራ ፍርፍር"),

                "ዓሳ ወጥ" to completedOrderRepository.getFoodCount("ዓሳ ወጥ"),
                "ዓሳ ለብለብ" to completedOrderRepository.getFoodCount("ዓሳ ለብለብ"),
                "ዓሳ ጎረድ" to completedOrderRepository.getFoodCount("ዓሳ ጎረድ"),
                "ዓሳ ጉላሽ" to completedOrderRepository.getFoodCount("ዓሳ ጉላሽ"),
                "ዓሳ ኮተሌት" to completedOrderRepository.getFoodCount("ዓሳ ኮተሌት"),
                "ዓሳ ሸክላ" to completedOrderRepository.getFoodCount("ዓሳ ሸክላ"),
                "ዓሳ ዱለት" to completedOrderRepository.getFoodCount("ዓሳ ዱለት"),
                "ዓሳ ሳንዱች" to completedOrderRepository.getFoodCount("ዓሳ ሳንዱች"),
                "ዓሳ ቋንጣ ፍርፍር" to completedOrderRepository.getFoodCount("ዓሳ ቋንጣ ፍርፍር"),
                "ዓሳ ክትፎ" to completedOrderRepository.getFoodCount("ዓሳ ክትፎ"),

                "ክለብ ሳንዱች" to completedOrderRepository.getFoodCount("ክለብ ሳንዱች"),
                "ስፔሻል ክለብ ሳንዱች" to completedOrderRepository.getFoodCount("ስፔሻል ክለብ ሳንዱች"),
                "ቢፍ ክለብ ሳንዱች" to completedOrderRepository.getFoodCount("ቢፍ ክለብ ሳንዱች"),
                "ቱና ክለብ ሳንዱች" to completedOrderRepository.getFoodCount("ቱና ክለብ ሳንዱች"),
                "ቺዝ ክለብ ሳንዱች" to completedOrderRepository.getFoodCount("ቺዝ ክለብ ሳንዱች"),
                "አትክልት ሳንዱች" to completedOrderRepository.getFoodCount("አትክልት ሳንዱች"),
                "ቺዝ ሳንዱች" to completedOrderRepository.getFoodCount("ቺዝ ሳንዱች"),
                "ቱና ሳንዱች" to completedOrderRepository.getFoodCount("ቱና ሳንዱች"),
                "ስፔሻል ሳንዱች" to completedOrderRepository.getFoodCount("ስፔሻል ሳንዱች"),
                "ኖርማል እንቁላል ሳንዱች" to completedOrderRepository.getFoodCount("ኖርማል እንቁላል ሳንዱች"),
                "ስፔሻል ቱና ሳንዱች" to completedOrderRepository.getFoodCount("ስፔሻል ቱና ሳንዱች"),
                "ባናና ሳንዱች" to completedOrderRepository.getFoodCount("ባናና ሳንዱች"),
                "ችፕስ" to completedOrderRepository.getFoodCount("ችፕስ"),

                "ስፔሻል ፒዛ" to completedOrderRepository.getFoodCount("ስፔሻል ፒዛ"),
                "ፍሩት ፒዛ" to completedOrderRepository.getFoodCount("ፍሩት ፒዛ"),
                "ቱና ፒዛ" to completedOrderRepository.getFoodCount("ቱና ፒዛ"),
                "አትክልት ፒዛ" to completedOrderRepository.getFoodCount("አትክልት ፒዛ"),
                "መሽሩም ፒዛ" to completedOrderRepository.getFoodCount("መሽሩም ፒዛ"),
                "ስፔሻል አትክልት ፒዛ" to completedOrderRepository.getFoodCount("ስፔሻል አትክልት ፒዛ"),
                "አትክልት በቱና ፒዛ" to completedOrderRepository.getFoodCount("አትክልት በቱና ፒዛ"),
                "ቱና ዊዝ ቺዝ ፒዛ" to completedOrderRepository.getFoodCount("ቱና ዊዝ ቺዝ ፒዛ"),
                "ማርጋሪታ ፒዛ" to completedOrderRepository.getFoodCount("ማርጋሪታ ፒዛ"),
                "ፎርኮርነር ፒዛ" to completedOrderRepository.getFoodCount("ፎርኮርነር ፒዛ"),
                "አቢሲኒያ ፒዛ" to completedOrderRepository.getFoodCount("አቢሲኒያ ፒዛ"),
                "የቤቱ ስፔሻል ፒዛ" to completedOrderRepository.getFoodCount("የቤቱ ስፔሻል ፒዛ"),
                "ሚኒ ፒዛ" to completedOrderRepository.getFoodCount("ሚኒ ፒዛ"),
                "ላርጅ ፒዛ" to completedOrderRepository.getFoodCount("ላርጅ ፒዛ"),
                "ኦሬንታል ፒዛ" to completedOrderRepository.getFoodCount("ኦሬንታል ፒዛ"),

                "ኖርማል በርገር" to completedOrderRepository.getFoodCount("ኖርማል በርገር"),
                "ደብል ስፔሻል ቱና በርገር" to completedOrderRepository.getFoodCount("ደብል ስፔሻል ቱና በርገር"),
                "ደብል ስፔሻል በርገር" to completedOrderRepository.getFoodCount("ደብል ስፔሻል በርገር"),
                "ደብል ኖርማል በርገር" to completedOrderRepository.getFoodCount("ደብል ኖርማል በርገር"),
                "ስፔሻል በርገር" to completedOrderRepository.getFoodCount("ስፔሻል በርገር"),
                "ቺዝ በርገር" to completedOrderRepository.getFoodCount("ቺዝ በርገር"),
                "ኖርማል አትክልት በርገር" to completedOrderRepository.getFoodCount("ኖርማል አትክልት በርገር"),
                "ስፔሻል አትክልት በርገር" to completedOrderRepository.getFoodCount("ስፔሻል አትክልት በርገር"),
                "ቱና በርገር" to completedOrderRepository.getFoodCount("ቱና በርገር"),
                "ኖርማል ዓሳ በርገር" to completedOrderRepository.getFoodCount("ኖርማል ዓሳ በርገር"),
                "ስፔሻል ዓሳ በርገር" to completedOrderRepository.getFoodCount("ስፔሻል ዓሳ በርገር"),
                "ቺዝ ዓሳ በርገር" to completedOrderRepository.getFoodCount("ቺዝ ዓሳ በርገር"),
                "ደብል ስፔሻል ዓሳ በርገር" to completedOrderRepository.getFoodCount("ደብል ስፔሻል ዓሳ በርገር"),
                "ደብል ኖርማል ዓሳ በርገር" to completedOrderRepository.getFoodCount("ደብል ኖርማል ዓሳ በርገር"),
                "ካቻፕ" to completedOrderRepository.getFoodCount("ካቻፕ")
            )
            val categoryCounts = foodCategories.mapValues { (_, items) ->
                items.sumOf { item -> counts[item] ?: 0 }
            }

            val total = counts.values.sum()

            withContext(Dispatchers.Main) {
                _foodCounts.value = counts
                _categoryCounts.value = categoryCounts
                _totalCount.value = total
            }
        }
    }
}