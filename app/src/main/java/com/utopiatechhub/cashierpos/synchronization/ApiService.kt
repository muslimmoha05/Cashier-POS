package com.utopiatechhub.cashierpos.synchronization

import com.utopiatechhub.cashierpos.data.FoodCategory
import com.utopiatechhub.cashierpos.data.Food
import com.utopiatechhub.cashierpos.data.Waiter
import retrofit2.http.GET
import retrofit2.Response

interface ApiService {
    @GET("sync_waiters.php")
    suspend fun getWaiters(): Response<List<Waiter>>

    @GET("sync_foods.php")
    suspend fun getFoods(): Response<List<Food>>

    @GET("sync_food_categories.php")
    suspend fun getFoodCategories(): Response<List<FoodCategory>>
}
