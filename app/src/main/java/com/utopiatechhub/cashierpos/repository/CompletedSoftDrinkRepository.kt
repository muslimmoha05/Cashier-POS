package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.CompletedSoftDrinkDao
import com.utopiatechhub.cashierpos.data.CompletedSoftDrink
import kotlinx.coroutines.flow.Flow

class CompletedSoftDrinkRepository(private val completedSoftDrinkDao: CompletedSoftDrinkDao) {

    suspend fun insert(completedSoftDrink: CompletedSoftDrink) {
        completedSoftDrinkDao.insert(completedSoftDrink)
    }

    suspend fun delete(completedSoftDrink: CompletedSoftDrink) {
        completedSoftDrinkDao.delete(completedSoftDrink)
    }

    suspend fun clearCompletedSoftDrinks() {
        completedSoftDrinkDao.clearCompletedSoftDrinks()
    }

    fun getAllCompletedSoftDrinks(): Flow<List<CompletedSoftDrink>> {
        return completedSoftDrinkDao.getAllCompletedSoftDrinks()
    }

    suspend fun getSoftDrinkCount(itemName: String): Int {
        return completedSoftDrinkDao.getSoftDrinkCount(itemName)
    }

    suspend fun getTotalSoftDrinkPrice(): Double {
        return completedSoftDrinkDao.getTotalSoftDrinkPrice()
    }

}