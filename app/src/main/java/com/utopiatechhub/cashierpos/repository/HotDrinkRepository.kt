package com.utopiatechhub.cashierpos.repository

import com.utopiatechhub.cashierpos.dao.HotDrinkDao
import com.utopiatechhub.cashierpos.data.HotDrink
import kotlinx.coroutines.flow.Flow

class HotDrinkRepository(private val hotDrinkDao: HotDrinkDao) {

    val allHotDrinks: Flow<List<HotDrink>> = hotDrinkDao.getAllHotDrinks()

    suspend fun insertHotDrink(hotDrink: HotDrink) {
        hotDrinkDao.insert(hotDrink)
    }

    suspend fun updateHotDrink(hotDrink: HotDrink) {
        hotDrinkDao.update(hotDrink)
    }

    suspend fun deleteHotDrink(hotDrink: HotDrink) {
        hotDrinkDao.delete(hotDrink)
    }

    suspend fun insertHotDrinksManually(hotDrink: List<HotDrink>) {
        hotDrinkDao.insertHotDrinksManually(hotDrink)
    }
}