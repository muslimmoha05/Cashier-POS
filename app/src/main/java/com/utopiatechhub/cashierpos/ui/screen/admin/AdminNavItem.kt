package com.utopiatechhub.cashierpos.ui.screen.admin

sealed class AdminNavItem(val title: String, val screenRoute: String) {
    // Menu Management
    data object ManageFood : AdminNavItem("Manage Food", "manage_food")
    data object ManageCake : AdminNavItem("Manage Cake", "manage_cake")
    data object ManageHotDrink : AdminNavItem("Manage Hot Drink", "manage_hot_drink")
    data object ManageSoftDrink : AdminNavItem("Manage Soft Drink", "manage_soft_drink")
    data object ManageBread : AdminNavItem("Manage Bread", "manage_bread")
    data object ManageJuice : AdminNavItem("Manage Juice", "manage_juice")
    data object ManagePackedFood : AdminNavItem("Manage Packed Food", "manage_packed_food")
    data object ManageFoodCategory : AdminNavItem("Manage Food Category", "manage_food_category")
    // Completed orders
    data object CompletedFoodOrder : AdminNavItem("Completed Order", "completed_order")
    data object CompletedCakeOrder : AdminNavItem("Completed Cake Order", "completed_cake_order")
    data object CompletedHotDrinkOrder : AdminNavItem("Completed Hot Drink Order", "completed_hot_drink_order")
    data object CompletedSoftDrinkOrder : AdminNavItem("Completed Soft Drink Order", "completed_soft_drink_order")
    data object CompletedBreadOrder : AdminNavItem("Completed Bread Order", "completed_bread_order")
    data object CompletedJuiceOrder : AdminNavItem("Completed Juice Order", "completed_juice_order")
    data object CompletedPackedFoodOrder : AdminNavItem("Completed Packed Order", "completed_packed_food_order")
    // Summary
    data object FoodSummary : AdminNavItem("Food Summary", "food_summary")
    data object CakeSummary : AdminNavItem("Cake Summary", "cake_summary")
    data object HotDrinkSummary : AdminNavItem("Hot Drink Summary", "hot_drink_summary")
    data object SoftDrinkSummary : AdminNavItem("Soft Drink Summary", "soft_drink_summary")
    data object BreadSummary : AdminNavItem("Bread Summary", "bread_summary")
    data object JuiceSummary : AdminNavItem("Juice Summary", "juice_summary")
    data object PackedFoodSummary : AdminNavItem("Packed Food Summary", "packed_food_summary")
    // Waiter and Cashier Management
    data object ManageWaiter : AdminNavItem("Manage Waiter", "manage_waiter")
    data object ManageCashier : AdminNavItem("Manage Cashier", "manage_cashier")
    // Others
    data object AdminSetting : AdminNavItem("Admin Setting", "admin_setting")
    data object ReceiverScreen : AdminNavItem("Receiver Screen", "receiver_screen")
}