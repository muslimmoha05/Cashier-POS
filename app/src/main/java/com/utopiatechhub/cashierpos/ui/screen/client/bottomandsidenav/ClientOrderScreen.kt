package com.utopiatechhub.cashierpos.ui.screen.client.bottomandsidenav

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.utopiatechhub.cashierpos.data.Order
import com.utopiatechhub.cashierpos.data.OtherOrder
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.repository.OrderRepository
import com.utopiatechhub.cashierpos.repository.OtherOrderRepository
import com.utopiatechhub.cashierpos.ui.theme.Teal
import com.utopiatechhub.cashierpos.viewmodel.OrderViewModel
import com.utopiatechhub.cashierpos.viewmodel.OtherOrderViewModel
import com.utopiatechhub.cashierpos.viewmodel.factory.OrderViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.OtherOrderViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientOrderScreen(navController: NavController) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context.applicationContext)

    // Food Order ViewModel
    val orderRepository = OrderRepository(database.orderDao())
    val orderViewModel: OrderViewModel = viewModel(factory = OrderViewModelFactory(orderRepository))
    val orders by orderViewModel.orders.collectAsState(initial = emptyList())

    // Other Order ViewModel
    val otherOrderRepository = OtherOrderRepository(database.otherOrderDao())
    val otherOrderViewModel: OtherOrderViewModel = viewModel(factory = OtherOrderViewModelFactory(otherOrderRepository))
    val otherOrders by otherOrderViewModel.otherOrders.collectAsState(initial = emptyList())

    // Group other orders by waiter
    val groupedOtherOrders = otherOrders.groupBy { it.waiterName }

    val handler = remember { Handler(Looper.getMainLooper()) }
    val checkOrdersRunnable = remember {
        object : Runnable {
            override fun run() {
                orderViewModel.fetchOrders()
                otherOrderViewModel.fetchOtherOrders() // Fetch other orders
                handler.postDelayed(this, 30000)
            }
        }
    }

    // Start order checking when the screen loads
    LaunchedEffect(Unit) {
        handler.post(checkOrdersRunnable)
    }

    // Stop the handler when the composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            handler.removeCallbacks(checkOrdersRunnable)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orders") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (orders.isEmpty() && otherOrders.isEmpty()) {
                Text(
                    text = "No incoming orders.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    content = {
                        // Display Food Orders
                        items(orders) { order ->
                            OrderFoodItemRow(
                                order = order,
                                onDelete = { orderViewModel.deleteOrder(order) }
                            )
                        }

                        // Display Grouped Other Orders
                        groupedOtherOrders.forEach { (waiterName, orders) ->
                            item {
                                OtherOrderGroupedCard(
                                    waiterName = waiterName,
                                    orders = orders,
                                    onDeleteAll = {
                                        orders.forEach { order ->
                                            otherOrderViewModel.deleteOtherOrder(order)
                                        }
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun OrderFoodItemRow(
    order: Order,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Order") },
            text = { Text("Are you sure you want to delete this order?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Teal) // Teal background
                ) {
                    Text("Delete", color = Color.White)
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                //  delete Icon and Waiter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "  ${order.waiterName} ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = { showDeleteDialog = true }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Order", tint = Teal)
                    }
                }

                // Food and Quantity
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = " ${order.foodName}      ",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "x       ${order.quantity }",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                // Total and Date
                Text(
                    text = " ${order.totalPrice} ",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = " ${order.orderDate} ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun OtherOrderGroupedCard(
    waiterName: String,
    orders: List<OtherOrder>,
    onDeleteAll: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    val totalPrice = orders.sumOf { it.totalPrice }
    val orderDate = orders.firstOrNull()?.orderDate ?: ""

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Order") },
            text = { Text("Are you sure you want to delete this order for $waiterName?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteAll()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Teal) // Teal background
                ) {
                    Text("Delete", color = Color.White)
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Waiter and Delete Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = " $waiterName ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = {
                        showDeleteDialog = true
                    }
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Order", tint = Teal)
                }
            }

            // Display each item with quantity
            orders.forEach { order ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = " ${order.itemName}      ",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "x       ${order.quantity}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Total Price
            Text(
                text = " $totalPrice ",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Order Date
            Text(
                text = " $orderDate ",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}