package com.utopiatechhub.cashierpos.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.repository.FoodCategoryRepository
import com.utopiatechhub.cashierpos.synchronization.SyncRepository
import com.utopiatechhub.cashierpos.ui.connection.ReceiverScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.AdminNavItem
import com.utopiatechhub.cashierpos.ui.screen.admin.AdminSettingScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.ManageManagerScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.ManageWaiterScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.bottomnav.AdminBottomNavItem
import com.utopiatechhub.cashierpos.ui.screen.admin.bottomnav.AdminCartScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.bottomnav.AdminDashboardScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.bottomnav.AdminMenuScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.bottomnav.AdminOrderScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.completedorders.CompletedBreadOrderScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.completedorders.CompletedCakeOrderScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.completedorders.CompletedHotDrinkOrderScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.completedorders.CompletedJuiceOrderScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.completedorders.CompletedOrderScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.completedorders.CompletedPackedFoodScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.completedorders.CompletedSoftDrinkOrderScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.managemenu.ManageBreadScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.managemenu.ManageCakeScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.managemenu.ManageFoodCategoryScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.managemenu.ManageFoodScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.managemenu.ManageHotDrinkScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.managemenu.ManageJuiceScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.managemenu.ManagePackedFoodScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.managemenu.ManageSoftDrinkScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.summary.BreadSummaryScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.summary.CakeSummaryScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.summary.FoodSummaryScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.summary.HotDrinkSummaryScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.summary.JuiceSummaryScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.summary.PackedFoodSummaryScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.summary.SoftDrinkSummaryScreen
import com.utopiatechhub.cashierpos.ui.theme.CashierPOSTheme
import com.utopiatechhub.cashierpos.viewmodel.FoodCategoryViewModel
import com.utopiatechhub.cashierpos.viewmodel.factory.FoodCategoryViewModelFactory

class AdminActivity : ComponentActivity() {

    // ActivityResultLauncher for requesting notification permission
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the ActivityResultLauncher for notification permission
        notificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Permission granted, you can now send notifications
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        // Check and request notification permission if needed
        checkAndRequestNotificationPermission()

        setContent {
            CashierPOSTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AdminScreen()
                }
            }
        }
    }

    // Function to check and request notification permission
    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = Manifest.permission.POST_NOTIFICATIONS

            // Check if the permission is already granted
            if (ContextCompat.checkSelfPermission(this, notificationPermission) != PackageManager.PERMISSION_GRANTED) {
                // Request the permission using the ActivityResultLauncher
                notificationPermissionLauncher.launch(notificationPermission)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AdminScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { AdminBottomNavigation(navController) }
    ) { innerPadding ->
        AdminNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AdminBottomNavigation(navController: NavHostController) {
    val navItems = listOf(
        AdminBottomNavItem.Dashboard,
        AdminBottomNavItem.Menu,
        AdminBottomNavItem.Cart,
        AdminBottomNavItem.Order
    )

    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = Color(0xFF00796B)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        navItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.screenRoute } == true
            val animatedScale = animateFloatAsState(targetValue = if (isSelected) 1.2f else 1f, label = "")

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (isSelected) Color.White else Color.LightGray,
                        modifier = Modifier.size(26.dp).scale(animatedScale.value)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (isSelected) Color.White else Color.LightGray,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AdminNavGraph(navController: NavHostController,
                  modifier: Modifier = Modifier ) {
    NavHost(
        navController = navController,
        startDestination = "admin_dashboard",
        modifier = modifier
    ) {
        // Menu Management screens
        composable(AdminNavItem.ManageFood.screenRoute) {
            ManageFoodScreen(navController)
        }
        composable(AdminNavItem.ManageCake.screenRoute) {
            ManageCakeScreen(navController)
        }
        composable(AdminNavItem.ManageHotDrink.screenRoute) {
            ManageHotDrinkScreen(navController)
        }
        composable(AdminNavItem.ManageSoftDrink.screenRoute) {
            ManageSoftDrinkScreen(navController)
        }
        composable(AdminNavItem.ManageBread.screenRoute) {
            ManageBreadScreen(navController)
        }
        composable(AdminNavItem.ManageJuice.screenRoute) {
            ManageJuiceScreen(navController)
        }
        composable(AdminNavItem.ManagePackedFood.screenRoute) {
            ManagePackedFoodScreen(navController)
        }
        composable(AdminNavItem.ManageFoodCategory.screenRoute) {
            val context = LocalContext.current
            val database = remember { AppDatabase.getDatabase(context) }
            val foodCategoryDao = remember { database.foodCategoryDao() }
            val foodCategoryRepository = remember { FoodCategoryRepository(foodCategoryDao) }
            val foodCategoryViewModel: FoodCategoryViewModel =
                viewModel(factory = FoodCategoryViewModelFactory(foodCategoryRepository))
            ManageFoodCategoryScreen(navController, foodCategoryViewModel, context)
        }

        // Completed Order Screens
        composable(AdminNavItem.CompletedFoodOrder.screenRoute) {
            CompletedOrderScreen(navController)
        }
        composable(AdminNavItem.CompletedCakeOrder.screenRoute) {
            CompletedCakeOrderScreen(navController)
        }
        composable(AdminNavItem.CompletedHotDrinkOrder.screenRoute) {
            CompletedHotDrinkOrderScreen(navController)
        }
        composable(AdminNavItem.CompletedSoftDrinkOrder.screenRoute) {
            CompletedSoftDrinkOrderScreen(navController)
        }
        composable(AdminNavItem.CompletedBreadOrder.screenRoute) {
            CompletedBreadOrderScreen(navController)
        }
        composable(AdminNavItem.CompletedPackedFoodOrder.screenRoute) {
            CompletedPackedFoodScreen(navController)
        }
        composable(AdminNavItem.CompletedJuiceOrder.screenRoute) {
            CompletedJuiceOrderScreen(navController)
        }

        // Bottom Navigation Screens
        composable("admin_dashboard") {
            AdminDashboardScreen(
                navController = navController
            )
        }
        composable("admin_menu") {
            AdminMenuScreen()
        }
        composable("admin_cart") {
            AdminCartScreen(navController)
        }
        composable("admin_order") {
            AdminOrderScreen(navController)
        }

        // Summary Screens
        composable(AdminNavItem.FoodSummary.screenRoute) {
            FoodSummaryScreen()
        }
        composable(AdminNavItem.CakeSummary.screenRoute) {
            CakeSummaryScreen()
        }
        composable(AdminNavItem.HotDrinkSummary.screenRoute) {
            HotDrinkSummaryScreen()
        }
        composable(AdminNavItem.SoftDrinkSummary.screenRoute) {
            SoftDrinkSummaryScreen()
        }
        composable(AdminNavItem.BreadSummary.screenRoute) {
            BreadSummaryScreen()
        }
        composable(AdminNavItem.JuiceSummary.screenRoute) {
            JuiceSummaryScreen()
        }
        composable(AdminNavItem.PackedFoodSummary.screenRoute) {
            PackedFoodSummaryScreen()
        }

        // Others
        composable(AdminNavItem.AdminSetting.screenRoute) {
            val context = LocalContext.current
            val syncRepository = SyncRepository(context)
            AdminSettingScreen(syncRepository)
        }
        composable(AdminNavItem.ReceiverScreen.screenRoute) {
            ReceiverScreen()
        }
        composable(AdminNavItem.ManageWaiter.screenRoute) {
            ManageWaiterScreen(navController)
        }
        composable(AdminNavItem.ManageCashier.screenRoute) {
            ManageManagerScreen(navController)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun AdminScreenPreview() {
    CashierPOSTheme {
        AdminScreen()
    }
}