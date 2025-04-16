package com.utopiatechhub.cashierpos.ui.screen.admin.bottomnav

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Cookie
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.utopiatechhub.cashierpos.R
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.repository.CompletedBreadRepository
import com.utopiatechhub.cashierpos.repository.CompletedCakeRepository
import com.utopiatechhub.cashierpos.repository.CompletedHotDrinkRepository
import com.utopiatechhub.cashierpos.repository.CompletedJuiceRepository
import com.utopiatechhub.cashierpos.repository.CompletedOrderRepository
import com.utopiatechhub.cashierpos.repository.CompletedPackedFoodRepository
import com.utopiatechhub.cashierpos.repository.CompletedSoftDrinkRepository
import com.utopiatechhub.cashierpos.ui.theme.CashierPOSTheme
import com.utopiatechhub.cashierpos.ui.theme.Teal
import com.utopiatechhub.cashierpos.viewmodel.CompletedBreadViewModel
import com.utopiatechhub.cashierpos.viewmodel.CompletedCakeViewModel
import com.utopiatechhub.cashierpos.viewmodel.CompletedHotDrinkViewModel
import com.utopiatechhub.cashierpos.viewmodel.CompletedJuiceViewModel
import com.utopiatechhub.cashierpos.viewmodel.CompletedOrderViewModel
import com.utopiatechhub.cashierpos.viewmodel.CompletedPackedFoodViewModel
import com.utopiatechhub.cashierpos.viewmodel.CompletedSoftDrinkViewModel
import com.utopiatechhub.cashierpos.viewmodel.factory.CompletedBreadViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.CompletedCakeViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.CompletedHotDrinkViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.CompletedJuiceViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.CompletedOrderViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.CompletedPackedFoodViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.CompletedSoftDrinkViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(navController: NavHostController) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context.applicationContext)

    // Repositories
    val completedBreadRepository = CompletedBreadRepository(database.completedBreadDao())
    val completedCakeRepository = CompletedCakeRepository(database.completedCakeDao())
    val completedHotDrinkRepository = CompletedHotDrinkRepository(database.completedHotDrinkDao())
    val completedSoftDrinkRepository = CompletedSoftDrinkRepository(database.completedSoftDrinkDao())
    val completedJuiceRepository = CompletedJuiceRepository(database.completedJuiceDao())
    val completedPackedFoodRepository = CompletedPackedFoodRepository(database.completedPackedFoodDao())
    val completedOrderRepository = CompletedOrderRepository(database.completedOrderDao(), database.orderDao())

    // ViewModels
    val completedBreadViewModel: CompletedBreadViewModel = viewModel(factory = CompletedBreadViewModelFactory(completedBreadRepository))
    val completedCakeViewModel: CompletedCakeViewModel = viewModel(factory = CompletedCakeViewModelFactory(completedCakeRepository))
    val completedHotDrinkViewModel: CompletedHotDrinkViewModel = viewModel(factory = CompletedHotDrinkViewModelFactory(completedHotDrinkRepository))
    val completedSoftDrinkViewModel: CompletedSoftDrinkViewModel = viewModel(factory = CompletedSoftDrinkViewModelFactory(completedSoftDrinkRepository))
    val completedJuiceViewModel: CompletedJuiceViewModel = viewModel(factory = CompletedJuiceViewModelFactory(completedJuiceRepository))
    val completedPackedFoodViewModel: CompletedPackedFoodViewModel = viewModel(factory = CompletedPackedFoodViewModelFactory(completedPackedFoodRepository))
    val completedOrderViewModel: CompletedOrderViewModel = viewModel(factory = CompletedOrderViewModelFactory(completedOrderRepository))

    var showClearAllConfirmationDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.admin_dashboard),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate("admin_setting") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            DashboardHeader()

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                DashboardSection(title = stringResource(R.string.menu_management)) {
                    ManageMenuButton(navController)
                }

                DashboardSection(title = stringResource(R.string.orders_text)) {
                    CompletedOrdersButton(
                        navController = navController,
                        onClearAll = { showClearAllConfirmationDialog = true }
                    )
                }

                DashboardSection(title = stringResource(R.string.reports)) {
                    SummaryButton(navController)
                }

                DashboardSection(title = stringResource(R.string.staff_management)) {
                    DashboardButton(
                        text = stringResource(R.string.waiters),
                        icon = Icons.Default.Person,
                        onClick = { navController.navigate("manage_waiter") }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    DashboardButton(
                        text = stringResource(R.string.cashiers),
                        icon = Icons.Default.Person,
                        onClick = { navController.navigate("manage_cashier") }
                    )
                }
            }
        }
    }

    if (showClearAllConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showClearAllConfirmationDialog = false },
            icon = {
                Icon(
                    Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(
                    text = stringResource(R.string.clear_all_orders),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.clear_orders_message),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        completedBreadViewModel.clearCompletedBreads()
                        completedCakeViewModel.clearCompletedCakes()
                        completedHotDrinkViewModel.clearCompletedHotDrinks()
                        completedSoftDrinkViewModel.clearCompletedSoftDrinks()
                        completedJuiceViewModel.clearCompletedJuices()
                        completedPackedFoodViewModel.clearAllCompletedPackedFoods()
                        completedOrderViewModel.clearCompletedOrders()
                        showClearAllConfirmationDialog = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.clear_all),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showClearAllConfirmationDialog = false }
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            shape = MaterialTheme.shapes.extraLarge,
            containerColor = MaterialTheme.colorScheme.surface,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun DashboardHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.welcome_admin),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.intro_title),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun DashboardSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
fun DashboardButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ManageMenuButton(navController: NavHostController) {
    val menuItems = listOf(
        Triple(stringResource(R.string.food_category), Icons.Default.Category, "manage_food_category"),
        Triple(stringResource(R.string.food), Icons.Default.Fastfood, "manage_food"),
        Triple(stringResource(R.string.cake), Icons.Default.Cake, "manage_cake"),
        Triple(stringResource(R.string.bread), Icons.Default.BakeryDining, "manage_bread"),
        Triple(stringResource(R.string.juice), Icons.Default.LocalDrink, "manage_juice"),
        Triple(stringResource(R.string.hot_drink), Icons.Default.LocalCafe, "manage_hot_drink"),
        Triple(stringResource(R.string.soft_drink), Icons.Default.LocalBar, "manage_soft_drink"),
        Triple(stringResource(R.string.packed_food), Icons.Default.Cookie, "manage_packed_food")
    )

    ExpandableSection(
        title = stringResource(R.string.menu_text),
        icon = Icons.Default.Menu,
        items = menuItems,
        navController = navController
    )
}

@Composable
fun CompletedOrdersButton(
    navController: NavHostController,
    onClearAll: () -> Unit,
    title: String = stringResource(R.string.completed_orders_text)
) {
    val completedOrders = listOf(
        Triple(stringResource(R.string.food_orders), Icons.Default.Fastfood, "completed_order"),
        Triple(stringResource(R.string.cake_orders), Icons.Default.Cake, "completed_cake_order"),
        Triple(stringResource(R.string.hot_drink_orders), Icons.Default.LocalCafe, "completed_hot_drink_order"),
        Triple(stringResource(R.string.soft_drink_orders), Icons.Default.LocalBar, "completed_soft_drink_order"),
        Triple(stringResource(R.string.bread_orders), Icons.Default.BakeryDining, "completed_bread_order"),
        Triple(stringResource(R.string.juice_orders), Icons.Default.LocalDrink, "completed_juice_order"),
        Triple(stringResource(R.string.packed_food_orders), Icons.Default.Cookie, "completed_packed_food_order")
    )

    ExpandableSectionWithClear(
        title = title,
        icon = Icons.Default.DoneOutline,
        items = completedOrders,
        navController = navController,
        onClearAll = onClearAll
    )
}


@Composable
fun SummaryButton(navController: NavHostController) {
    val summaryItems = listOf(
        Triple(stringResource(R.string.food_summary), Icons.Default.Fastfood, "food_summary"),
        Triple(stringResource(R.string.cake_summary), Icons.Default.Cake, "cake_summary"),
        Triple(stringResource(R.string.hot_drink_summary), Icons.Default.LocalCafe, "hot_drink_summary"),
        Triple(stringResource(R.string.soft_drink_summary), Icons.Default.LocalBar, "soft_drink_summary"),
        Triple(stringResource(R.string.bread_summary), Icons.Default.BakeryDining, "bread_summary"),
        Triple(stringResource(R.string.juice_summary), Icons.Default.LocalDrink, "juice_summary"),
        Triple(stringResource(R.string.packed_food_summary), Icons.Default.Cookie, "packed_food_summary")
    )

    ExpandableSection(
        title = stringResource(R.string.summary_text),
        icon = Icons.Default.BarChart,
        items = summaryItems,
        navController = navController
    )
}


@Composable
private fun ExpandableSection(
    title: String,
    icon: ImageVector,
    items: List<Triple<String, ImageVector, String>>,
    navController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column {
            ListItem(
                headlineContent = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                },
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            if (expanded) {
                Column {
                    items.forEach { (label, icon, route) ->
                        ListItem(
                            headlineContent = { Text(text = label) },
                            leadingContent = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(route)
                                    expanded = false
                                }
                                .padding(start = 32.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 32.dp),
                            thickness = 0.5.dp,
                            color = Teal
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpandableSectionWithClear(
    title: String,
    icon: ImageVector,
    items: List<Triple<String, ImageVector, String>>,
    navController: NavHostController,
    onClearAll: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column {
            ListItem(
                headlineContent = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                trailingContent = {
                    Row {
                        IconButton(
                            onClick = { onClearAll() },
                            modifier = Modifier.semantics {
                                contentDescription = "Clear All Completed Orders?"
                                role = androidx.compose.ui.semantics.Role.Button
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DeleteSweep,
                                contentDescription = stringResource(R.string.clear_all),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "Collapse" else "Expand"
                        )
                    }
                },
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            if (expanded) {
                Column {
                    items.forEach { (label, icon, route) ->
                        ListItem(
                            headlineContent = { Text(text = label) },
                            leadingContent = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(route)
                                    expanded = false
                                }
                                .padding(start = 32.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 32.dp),
                            thickness = 0.5.dp,
                            color = Teal
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AdminDashboardScreenPreview() {
    val navController = rememberNavController()
    CashierPOSTheme {
        AdminDashboardScreen(navController = navController)
    }
}