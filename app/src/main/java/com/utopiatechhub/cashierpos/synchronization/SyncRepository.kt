package com.utopiatechhub.cashierpos.synchronization

import android.content.Context
import com.utopiatechhub.cashierpos.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncRepository(context: Context) {
    private val waiterDao = AppDatabase.getDatabase(context).waiterDao()
    private val foodDao = AppDatabase.getDatabase(context).foodDao()
    private val foodCategoryDao = AppDatabase.getDatabase(context).foodCategoryDao()

    fun syncData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Fetch waiters
                val waiterResponse = RetrofitClient.instance.getWaiters()
                if (waiterResponse.isSuccessful) {
                    waiterResponse.body()?.let { waiters ->
                        waiterDao.insertAll(waiters)
                    }
                } else {
                    // Handle the error response
                }

                // Fetch foods
                val foodResponse = RetrofitClient.instance.getFoods()
                if (foodResponse.isSuccessful) {
                    foodResponse.body()?.let { foods ->
                        foodDao.insertAll(foods)
                    }
                } else {
                    // Handle the error response
                }

                // Fetch food categories
                val foodCategoryResponse = RetrofitClient.instance.getFoodCategories()
                if (foodCategoryResponse.isSuccessful) {
                    foodCategoryResponse.body()?.let { foodCategories ->
                        foodCategoryDao.insertAll(foodCategories)
                    }
                } else {
                    // Handle the error response
                }
            } catch (e: Exception) {
                // Handle exceptions (e.g., network errors)
            }
        }
    }
}