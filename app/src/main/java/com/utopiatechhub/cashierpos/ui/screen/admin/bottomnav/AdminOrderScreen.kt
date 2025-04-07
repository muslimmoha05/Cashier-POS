package com.utopiatechhub.cashierpos.ui.screen.admin.bottomnav

import android.Manifest
import android.app.Activity
// import android.app.NotificationManager
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.Download
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
import androidx.core.app.ActivityCompat
// import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
// import com.utopiatechhub.cashierpos.R
import com.utopiatechhub.cashierpos.data.CompletedBread
import com.utopiatechhub.cashierpos.data.CompletedCake
import com.utopiatechhub.cashierpos.data.CompletedHotDrink
import com.utopiatechhub.cashierpos.data.CompletedJuice
import com.utopiatechhub.cashierpos.data.CompletedOrder
import com.utopiatechhub.cashierpos.data.CompletedSoftDrink
import com.utopiatechhub.cashierpos.data.Order
import com.utopiatechhub.cashierpos.data.OtherOrder
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.printer.BluetoothPrinterManager
import com.utopiatechhub.cashierpos.repository.CompletedBreadRepository
import com.utopiatechhub.cashierpos.repository.CompletedCakeRepository
import com.utopiatechhub.cashierpos.repository.CompletedHotDrinkRepository
import com.utopiatechhub.cashierpos.repository.CompletedJuiceRepository
import com.utopiatechhub.cashierpos.repository.CompletedOrderRepository
import com.utopiatechhub.cashierpos.repository.CompletedSoftDrinkRepository
import com.utopiatechhub.cashierpos.repository.OrderRepository
import com.utopiatechhub.cashierpos.repository.OtherOrderRepository
import com.utopiatechhub.cashierpos.ui.theme.Teal
import com.utopiatechhub.cashierpos.viewmodel.CompletedBreadViewModel
import com.utopiatechhub.cashierpos.viewmodel.CompletedCakeViewModel
import com.utopiatechhub.cashierpos.viewmodel.CompletedHotDrinkViewModel
import com.utopiatechhub.cashierpos.viewmodel.CompletedJuiceViewModel
import com.utopiatechhub.cashierpos.viewmodel.CompletedOrderViewModel
import com.utopiatechhub.cashierpos.viewmodel.CompletedSoftDrinkViewModel
import com.utopiatechhub.cashierpos.viewmodel.factory.CompletedOrderViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.OrderViewModel
import com.utopiatechhub.cashierpos.viewmodel.OtherOrderViewModel
import com.utopiatechhub.cashierpos.viewmodel.factory.CompletedBreadViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.CompletedCakeViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.CompletedHotDrinkViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.CompletedJuiceViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.CompletedSoftDrinkViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.OrderViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.OtherOrderViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.forEach

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrderScreen(navController: NavController) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context.applicationContext)
    val orderRepository = OrderRepository(database.orderDao())
    val completedOrderRepository = CompletedOrderRepository(database.completedOrderDao(), database.orderDao())
    val orderViewModel: OrderViewModel = viewModel(factory = OrderViewModelFactory(orderRepository))
    val completedOrderViewModel: CompletedOrderViewModel = viewModel(factory = CompletedOrderViewModelFactory(completedOrderRepository))

    // Repositories
    val completedCakeRepository = CompletedCakeRepository(database.completedCakeDao())
    val completedHotDrinkRepository = CompletedHotDrinkRepository(database.completedHotDrinkDao())
    val completedSoftDrinkRepository = CompletedSoftDrinkRepository(database.completedSoftDrinkDao())
    val completedBreadRepository = CompletedBreadRepository(database.completedBreadDao())
    val completedJuiceRepository = CompletedJuiceRepository(database.completedJuiceDao())

    // Other Order ViewModel
    val otherOrderRepository = OtherOrderRepository(database.otherOrderDao())
    val otherOrderViewModel: OtherOrderViewModel = viewModel(factory = OtherOrderViewModelFactory(otherOrderRepository))
    val otherOrders by otherOrderViewModel.otherOrders.collectAsState(initial = emptyList())


    // ViewModels for completed tables
    val completedCakeViewModel: CompletedCakeViewModel = viewModel(factory = CompletedCakeViewModelFactory(completedCakeRepository))
    val completedHotDrinkViewModel: CompletedHotDrinkViewModel = viewModel(factory = CompletedHotDrinkViewModelFactory(completedHotDrinkRepository))
    val completedSoftDrinkViewModel: CompletedSoftDrinkViewModel = viewModel(factory = CompletedSoftDrinkViewModelFactory(completedSoftDrinkRepository))
    val completedBreadViewModel: CompletedBreadViewModel = viewModel(factory = CompletedBreadViewModelFactory(completedBreadRepository))
    val completedJuiceViewModel: CompletedJuiceViewModel = viewModel(factory = CompletedJuiceViewModelFactory(completedJuiceRepository))

    // Handle completion logic
    val onCompleteOrder: (List<OtherOrder>) -> Unit = { orders ->
        orders.forEach { order ->
            when (order.itemType) {
                "cake" -> {
                    val completedCake = CompletedCake(
                        waiterName = order.waiterName,
                        itemName = order.itemName,
                        quantity = order.quantity,
                        totalPrice = order.totalPrice,
                        orderDate = order.orderDate
                    )
                    completedCakeViewModel.insert(completedCake)
                }
                "hotDrink" -> {
                    val completedHotDrink = CompletedHotDrink(
                        waiterName = order.waiterName,
                        itemName = order.itemName,
                        quantity = order.quantity,
                        totalPrice = order.totalPrice,
                        orderDate = order.orderDate
                    )
                    completedHotDrinkViewModel.insert(completedHotDrink)
                }
                "softDrink" -> {
                    val completedSoftDrink = CompletedSoftDrink(
                        waiterName = order.waiterName,
                        itemName = order.itemName,
                        quantity = order.quantity,
                        totalPrice = order.totalPrice,
                        orderDate = order.orderDate
                    )
                    completedSoftDrinkViewModel.insert(completedSoftDrink)
                }
                "bread" -> {
                    val completedBread = CompletedBread(
                        waiterName = order.waiterName,
                        itemName = order.itemName,
                        quantity = order.quantity,
                        totalPrice = order.totalPrice,
                        orderDate = order.orderDate
                    )
                    completedBreadViewModel.insert(completedBread)
                }
                "juice" -> {
                    val completedJuice = CompletedJuice(
                        waiterName = order.waiterName,
                        itemName = order.itemName,
                        quantity = order.quantity,
                        totalPrice = order.totalPrice,
                        orderDate = order.orderDate
                    )
                    completedJuiceViewModel.insert(completedJuice)
                }
            }
            otherOrderViewModel.deleteOtherOrder(order)
        }
    }

    // Group other orders by waiter
    val groupedOtherOrders = otherOrders.groupBy { it.waiterName }

    val bluetoothPrinterManager = remember { BluetoothPrinterManager(context) }
    val orders by orderViewModel.orders.collectAsState(initial = emptyList())

    val handler = remember { Handler(Looper.getMainLooper()) }
    val checkOrdersRunnable = remember {
        object : Runnable {
            override fun run() {
                orderViewModel.fetchOrders()
                otherOrderViewModel.fetchOtherOrders()
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

    /*
    LaunchedEffect(orders.size) {
        if (orders.isNotEmpty()) {
            sendNotification(
                context,
                "New Order Received",
                "You have ${orders.size} new orders!"
            )
        }
    }
     */

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orders") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate("receiver_screen")
                        }
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Receive Order")
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
            // Display Orders or Empty State
            if (orders.isEmpty() && otherOrders.isEmpty()) {
                Text(
                    text = "No incoming orders.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn {
                    items(orders) { order ->
                        OrderItemRow(
                            order = order,
                            bluetoothPrinterManager = bluetoothPrinterManager,
                            orderViewModel = orderViewModel,
                            completedOrderViewModel = completedOrderViewModel,
                            onDelete = { orderViewModel.deleteOrder(order) }
                        )
                    }

                    // Display Grouped Other Orders
                    groupedOtherOrders.forEach { (waiterName, orders) ->
                        item {
                            AdminOtherOrderGroupedCard(
                                waiterName = waiterName,
                                orders = orders,
                                onDeleteAll = {
                                    orders.forEach { order ->
                                        otherOrderViewModel.deleteOtherOrder(order)
                                    }
                                },
                                onComplete = onCompleteOrder
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemRow(
    order: Order,
    bluetoothPrinterManager: BluetoothPrinterManager,
    orderViewModel: OrderViewModel,
    completedOrderViewModel: CompletedOrderViewModel,
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
                    colors = ButtonDefaults.buttonColors(containerColor = Teal)
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
                // Order ID and Waiter and Delete Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = " ${order.id}.",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "  ${order.waiterName} ",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = { showDeleteDialog = true }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Order", tint = Teal)
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))

                // Food and Quantity
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = " ${order.foodName}      ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "x       ${order.quantity }",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))

                // Total and Date
                Text(
                    text = " ${order.totalPrice} ",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(2.dp))

                // Print Order Button and delete Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = " ${order.orderDate} ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Button(
                        onClick = {
                            printOrder(order, bluetoothPrinterManager, orderViewModel, completedOrderViewModel)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("Print Order", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminOtherOrderGroupedCard(
    waiterName: String,
    orders: List<OtherOrder>,
    onDeleteAll: () -> Unit,
    onComplete: (List<OtherOrder>) -> Unit
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
                        text = " ${order.itemName}                      ",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "    x           ${order.quantity}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
            }

            // Total Price
            Text(
                text = " $totalPrice ",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 2.dp)
            )

            // Order Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = " $orderDate ",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
                IconButton(
                    onClick = {
                        onComplete(orders)
                    }
                ) {
                    Icon(Icons.Default.DoubleArrow, contentDescription = "Complete Order", tint = Teal)
                }
            }
        }
    }
}

/*
// Function to send a notification
fun sendNotification(context: Context, title: String, message: String) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "order_notifications"

    // Create a notification (compatible with Android O and above)
    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.baseline_notifications_active_24)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    // Send the notification
    notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
}
 */

// Function to ensure Bluetooth permissions are granted
fun ensureBluetoothPermissions(context: Context): Boolean {
    val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(Manifest.permission.BLUETOOTH_CONNECT)
    } else {
        arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN)
    }
    val missingPermissions = requiredPermissions.filter {
        ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
    }
    return if (missingPermissions.isNotEmpty()) {
        ActivityCompat.requestPermissions(context as Activity, missingPermissions.toTypedArray(), 100)
        false
    } else true
}

// Function to print an order
fun printOrder(
    order: Order,
    bluetoothPrinterManager: BluetoothPrinterManager,
    orderViewModel: OrderViewModel,
    completedOrderViewModel: CompletedOrderViewModel
) {
    if (!ensureBluetoothPermissions(bluetoothPrinterManager.context)) return

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val printerDevice: BluetoothDevice? = bluetoothPrinterManager.findPrinter("Inner printer")
            if (printerDevice == null || !bluetoothPrinterManager.connectToPrinter(printerDevice)) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(bluetoothPrinterManager.context, "Printer not found or connection failed.", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            val orderDetailsText = "            ${order.id}   :     ${order.waiterName}"
            val orderDetailsBitmap = bluetoothPrinterManager.textToImage(orderDetailsText, textSize = 32f) // Larger size
            bluetoothPrinterManager.printBitmap(orderDetailsBitmap)
            bluetoothPrinterManager.printAmharicText("_____________________________________________________")
            val foodDetailsText = "${order.foodName}      x      ${order.quantity}   =   ${order.totalPrice} ብር"
            bluetoothPrinterManager.printAmharicText(foodDetailsText)
            bluetoothPrinterManager.printAmharicText("_____________________________________________________")
            bluetoothPrinterManager.printAmharicText(" ${order.orderDate} ")
            bluetoothPrinterManager.printAmharicText("_____________________________________________________")
            bluetoothPrinterManager.printData("\n\n")

            withContext(Dispatchers.Main) {
                Toast.makeText(bluetoothPrinterManager.context, "Printed successfully.", Toast.LENGTH_SHORT).show()
                val completedOrder = CompletedOrder(
                    orderId = order.id,
                    waiterName = order.waiterName,
                    foodName = order.foodName,
                    quantity = order.quantity,
                    totalPrice = order.totalPrice,
                    orderDate = order.orderDate
                )
                completedOrderViewModel.insertCompletedOrder(completedOrder)
                orderViewModel.deleteOrder(order)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(bluetoothPrinterManager.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } finally {
            bluetoothPrinterManager.disconnect()
        }
    }
}