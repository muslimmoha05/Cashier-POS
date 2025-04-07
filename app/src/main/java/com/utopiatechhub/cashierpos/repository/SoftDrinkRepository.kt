package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.SoftDrinkDao
import com.utopiatechhub.cashierpos.data.SoftDrink
import kotlinx.coroutines.flow.Flow

class SoftDrinkRepository(private val softDrinkDao: SoftDrinkDao) {

    val allSoftDrinks: Flow<List<SoftDrink>> = softDrinkDao.getAllSoftDrinks()

    suspend fun insertSoftDrink(softDrink: SoftDrink) {
        softDrinkDao.insert(softDrink)
    }

    suspend fun updateSoftDrink(softDrink: SoftDrink) {
        softDrinkDao.update(softDrink)
    }

    suspend fun deleteSoftDrink(softDrink: SoftDrink) {
        softDrinkDao.delete(softDrink)
    }

    suspend fun insertSoftDrinksManually(softDrinks: List<SoftDrink>) {
        softDrinkDao.insertSoftDrinksManually(softDrinks)
    }
}