package com.utopiatechhub.cashierpos.ui.screen.client.bottomandsidenav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ClientBottomNavItem(val title: String, val icon: ImageVector, val screenRoute: String) {
    data object Dashboard : ClientBottomNavItem("Dashboard", Icons.Filled.Dashboard, "client_dashboard")
    data object Menu : ClientBottomNavItem("Menu", Icons.Filled.Menu, "menu")
    data object Cart : ClientBottomNavItem("Cart", Icons.Filled.ShoppingCart, "cart")
    data object Order : ClientBottomNavItem("Order", Icons.AutoMirrored.Filled.List, "client_order")
}