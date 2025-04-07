package com.utopiatechhub.cashierpos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.utopiatechhub.cashierpos.data.Cart
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cart: Cart)

    @Query("SELECT * FROM cart_table")
    fun getAllCartItems(): Flow<List<Cart>>

    @Query("DELETE FROM cart_table")
    suspend fun clearCart()

    @Delete
    suspend fun deleteCartItem(cart: Cart)
}