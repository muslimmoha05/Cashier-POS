package com.utopiatechhub.cashierpos.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.utopiatechhub.cashierpos.ui.connection.SenderScreen
import com.utopiatechhub.cashierpos.ui.screen.client.ClientNavItem
import com.utopiatechhub.cashierpos.ui.screen.client.bottomandsidenav.ClientBottomNavItem
import com.utopiatechhub.cashierpos.ui.screen.client.bottomandsidenav.ClientCartScreen
import com.utopiatechhub.cashierpos.ui.screen.client.bottomandsidenav.ClientDashboardScreen
import com.utopiatechhub.cashierpos.ui.screen.client.bottomandsidenav.ClientMenuScreen
import com.utopiatechhub.cashierpos.ui.screen.client.bottomandsidenav.ClientOrderScreen
import com.utopiatechhub.cashierpos.ui.theme.CashierPOSTheme

class ClientActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CashierPOSTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ClientScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ClientScreen() {
    val navController = rememberNavController()
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth(),
        bottomBar = {
            if (!isTablet) { // Show Bottom Navigation Bar for phones
                ClientBottomNavigation(navController)
            }
        }
    ) { innerPadding ->
        Row(modifier = Modifier.fillMaxSize()) {
            if (isTablet) { // Show Navigation Rail for tablets
                ClientNavigationRail(navController)
            }
            ClientNavGraph(
                navController = navController,
                modifier = Modifier
                    .padding(innerPadding)
                    .weight(1f)
            )
        }
    }
}

@Composable
fun ClientNavigationRail(navController: NavHostController) {
    val navItems = listOf(
        ClientBottomNavItem.Dashboard,
        ClientBottomNavItem.Menu,
        ClientBottomNavItem.Cart,
        ClientBottomNavItem.Order
    )

    // Get the current destination
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = Color(0xFF00796B)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            navItems.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any { it.route == item.screenRoute } == true
                val animatedScale = animateFloatAsState(targetValue = if (isSelected) 1.2f else 1f, label = "")

                NavigationRailItem(
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
                        if (currentDestination?.route == item.screenRoute) {
                            navController.popBackStack("menu", inclusive = true)
                        }
                        navController.navigate(item.screenRoute) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ClientBottomNavigation(navController: NavHostController) {
    val navItems = listOf(
        ClientBottomNavItem.Dashboard,
        ClientBottomNavItem.Menu,
        ClientBottomNavItem.Cart,
        ClientBottomNavItem.Order
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
                    if (currentDestination?.route == item.screenRoute) {
                        // Force reload menu screen by popping the stack and navigating again
                        navController.popBackStack("menu", inclusive = true)
                    }
                    navController.navigate(item.screenRoute) {
                        popUpTo(navController.graph.findStartDestination().id) { inclusive = false }
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
fun ClientNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "client_dashboard",
        modifier = modifier
    ) {
        composable("client_dashboard") {
            ClientDashboardScreen()
        }
        composable("menu") {
            ClientMenuScreen()
        }
        composable("cart") {
            ClientCartScreen(navController)
        }
        composable("client_order") {
            ClientOrderScreen(navController)
        }
        composable(ClientNavItem.SenderScreen.route) {
            SenderScreen()
        }
    }
}