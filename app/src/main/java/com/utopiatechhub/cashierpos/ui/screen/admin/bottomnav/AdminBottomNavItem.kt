package com.utopiatechhub.cashierpos.ui.screen.admin.bottomnav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AdminBottomNavItem(val title: String, val icon: ImageVector, val screenRoute: String) {
    data object Dashboard : AdminBottomNavItem("Dashboard", Icons.Filled.Dashboard, "admin_dashboard")
    data object Order : AdminBottomNavItem("Order", Icons.AutoMirrored.Filled.List, "admin_order")
    data object Menu : AdminBottomNavItem("Menu", Icons.Filled.Menu, "admin_menu")
    data object Cart : AdminBottomNavItem("Cart", Icons.Filled.ShoppingCart, "admin_cart")
}