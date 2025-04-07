package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.CompletedHotDrinkDao
import com.utopiatechhub.cashierpos.data.CompletedHotDrink
import kotlinx.coroutines.flow.Flow

class CompletedHotDrinkRepository(private val completedHotDrinkDao: CompletedHotDrinkDao) {

    suspend fun insert(completedHotDrink: CompletedHotDrink) {
        completedHotDrinkDao.insert(completedHotDrink)
    }

    suspend fun deleteCompletedHotDrink(completedHotDrink: CompletedHotDrink) {
        completedHotDrinkDao.deleteCompletedHotDrink(completedHotDrink)
    }

    suspend fun clearCompletedHotDrinks() {
        completedHotDrinkDao.clearCompletedHotDrinks()
    }

    fun getAllCompletedHotDrinks(): Flow<List<CompletedHotDrink>> {
        return completedHotDrinkDao.getAllCompletedHotDrinks()
    }

    suspend fun getHotDrinkCount(itemName: String): Int {
        return completedHotDrinkDao.getHotDrinkCount(itemName)
    }

    suspend fun getTotalHotDrinkPrice(): Double {
        return completedHotDrinkDao.getTotalHotDrinkPrice()
    }
}