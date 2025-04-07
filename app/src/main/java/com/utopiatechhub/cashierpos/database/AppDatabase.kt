package com.utopiatechhub.cashierpos.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.utopiatechhub.cashierpos.dao.BreadDao
import com.utopiatechhub.cashierpos.dao.CakeDao
import com.utopiatechhub.cashierpos.dao.CartDao
import com.utopiatechhub.cashierpos.dao.CompletedBreadDao
import com.utopiatechhub.cashierpos.dao.CompletedCakeDao
import com.utopiatechhub.cashierpos.dao.CompletedHotDrinkDao
import com.utopiatechhub.cashierpos.dao.CompletedJuiceDao
import com.utopiatechhub.cashierpos.dao.CompletedOrderDao
import com.utopiatechhub.cashierpos.dao.CompletedSoftDrinkDao
import com.utopiatechhub.cashierpos.dao.FoodCategoryDao
import com.utopiatechhub.cashierpos.dao.FoodDao
import com.utopiatechhub.cashierpos.dao.HotDrinkDao
import com.utopiatechhub.cashierpos.dao.JuiceDao
import com.utopiatechhub.cashierpos.dao.OrderDao
import com.utopiatechhub.cashierpos.dao.OtherOrderDao
import com.utopiatechhub.cashierpos.dao.SoftDrinkDao
import com.utopiatechhub.cashierpos.authentication.UserDao
import com.utopiatechhub.cashierpos.dao.WaiterDao
import com.utopiatechhub.cashierpos.data.Bread
import com.utopiatechhub.cashierpos.data.Cake
import com.utopiatechhub.cashierpos.data.Cart
import com.utopiatechhub.cashierpos.data.CompletedBread
import com.utopiatechhub.cashierpos.data.CompletedCake
import com.utopiatechhub.cashierpos.data.CompletedHotDrink
import com.utopiatechhub.cashierpos.data.CompletedJuice
import com.utopiatechhub.cashierpos.data.CompletedOrder
import com.utopiatechhub.cashierpos.data.CompletedSoftDrink
import com.utopiatechhub.cashierpos.data.Food
import com.utopiatechhub.cashierpos.data.FoodCategory
import com.utopiatechhub.cashierpos.data.FoodCategoryView
import com.utopiatechhub.cashierpos.data.HotDrink
import com.utopiatechhub.cashierpos.data.Juice
import com.utopiatechhub.cashierpos.data.Order
import com.utopiatechhub.cashierpos.data.OtherOrder
import com.utopiatechhub.cashierpos.data.SoftDrink
import com.utopiatechhub.cashierpos.authentication.User
import com.utopiatechhub.cashierpos.dao.CompletedPackedFoodDao
import com.utopiatechhub.cashierpos.dao.ManagerDao
import com.utopiatechhub.cashierpos.dao.PackedFoodDao
import com.utopiatechhub.cashierpos.data.CompletedPackedFood
import com.utopiatechhub.cashierpos.data.Manager
import com.utopiatechhub.cashierpos.data.PackedFood
import com.utopiatechhub.cashierpos.data.Waiter

@Database(
    entities = [
        User::class,
        Food::class, FoodCategory::class, Cake::class,
        Waiter::class, OtherOrder::class, Manager::class,
        Cart::class, Order::class, CompletedOrder::class,
        CompletedCake::class, CompletedHotDrink::class,
        CompletedSoftDrink::class, CompletedBread::class,
        CompletedJuice::class, CompletedPackedFood::class,
        HotDrink::class, SoftDrink::class, Bread::class,
        Juice::class, PackedFood::class ],
    views = [FoodCategoryView::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun managerDao(): ManagerDao
    abstract fun foodDao(): FoodDao
    abstract fun foodCategoryDao(): FoodCategoryDao
    abstract fun cakeDao(): CakeDao
    abstract fun waiterDao(): WaiterDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun otherOrderDao(): OtherOrderDao
    abstract fun completedOrderDao(): CompletedOrderDao
    abstract fun completedCakeDao(): CompletedCakeDao
    abstract fun completedHotDrinkDao(): CompletedHotDrinkDao
    abstract fun completedSoftDrinkDao(): CompletedSoftDrinkDao
    abstract fun completedBreadDao(): CompletedBreadDao
    abstract fun completedJuiceDao(): CompletedJuiceDao
    abstract fun completedPackedFoodDao(): CompletedPackedFoodDao
    abstract fun hotDrinkDao(): HotDrinkDao
    abstract fun softDrinkDao(): SoftDrinkDao
    abstract fun breadDao(): BreadDao
    abstract fun juiceDao(): JuiceDao
    abstract fun packedFoodDao(): PackedFoodDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cashier_pos_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}