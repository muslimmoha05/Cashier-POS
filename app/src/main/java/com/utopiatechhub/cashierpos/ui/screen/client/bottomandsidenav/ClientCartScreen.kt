package com.utopiatechhub.cashierpos.ui.screen.client.bottomandsidenav

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.utopiatechhub.cashierpos.authentication.TenantDataStore
import com.utopiatechhub.cashierpos.data.Cart
import com.utopiatechhub.cashierpos.data.OtherOrder
import com.utopiatechhub.cashierpos.data.Waiter
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.repository.CartRepository
import com.utopiatechhub.cashierpos.repository.OrderRepository
import com.utopiatechhub.cashierpos.repository.OtherOrderRepository
import com.utopiatechhub.cashierpos.repository.WaiterRepository
import com.utopiatechhub.cashierpos.ui.theme.Teal
import com.utopiatechhub.cashierpos.ui.theme.TealDark
import com.utopiatechhub.cashierpos.viewmodel.CartViewModel
import com.utopiatechhub.cashierpos.viewmodel.OrderViewModel
import com.utopiatechhub.cashierpos.viewmodel.OtherOrderViewModel
import com.utopiatechhub.cashierpos.viewmodel.WaiterViewModel
import com.utopiatechhub.cashierpos.viewmodel.factory.CartViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.OrderViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.OtherOrderViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.WaiterViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientCartScreen(navController: NavController ) {
    var selectedWaiter by remember { mutableStateOf<Waiter?>(null) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    var isButtonClicked by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context.applicationContext)
    val tenantDataStore = TenantDataStore(context)
    val cartRepository = CartRepository(database.cartDao())
    val waiterRepository = WaiterRepository(database.waiterDao())
    val orderRepository = OrderRepository(database.orderDao())
    val otherOrderRepository = OtherOrderRepository(database.otherOrderDao())

    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(cartRepository))
    val waiterViewModel: WaiterViewModel = viewModel(factory = WaiterViewModelFactory(waiterRepository, tenantDataStore))
    val orderViewModel: OrderViewModel = viewModel(factory = OrderViewModelFactory(orderRepository))
    val otherOrderViewModel: OtherOrderViewModel = viewModel(factory = OtherOrderViewModelFactory(otherOrderRepository))

    val cartItems by cartViewModel.cartItems.collectAsState(initial = emptyList())
    val totalPrice = cartItems.sumOf { it.foodPrice * it.quantity }
    val totalCakePrice = cartItems.sumOf { it.cakePrice * it.cakeQuantity }
    val totalHotDrinkPrice = cartItems.sumOf { it.hotDrinkPrice * it.hotDrinkQuantity }
    val totalSoftDrinkPrice = cartItems.sumOf { it.softDrinkPrice * it.softDrinkQuantity }
    val totalBreadPrice = cartItems.sumOf { it.breadPrice * it.breadQuantity }
    val totalJuicePrice = cartItems.sumOf { it.juicePrice * it.juiceQuantity }
    val veryTotalPrice = totalPrice + totalCakePrice + totalHotDrinkPrice + totalSoftDrinkPrice + totalBreadPrice + totalJuicePrice

    LaunchedEffect(Unit) {
        cartViewModel.fetchCartItems()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cart") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            cartViewModel.clearCart()
                            Toast.makeText(context, "Cart cleared", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear Cart")
                    }
                    IconButton(
                        onClick = { navController.navigate("sender_screen") }
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Sender Screen")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (cartItems.isEmpty()) {
                Text(
                    text = "Your cart is empty.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Total Price:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
                    Text(
                        currencyFormat.format(veryTotalPrice),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(cartItems) { cartItem ->
                            when {
                                !cartItem.foodName.isNullOrBlank() -> {
                                    CartItemRow(
                                        cartItem = cartItem,
                                        onUpdateQuantity = { newQuantity ->
                                            cartViewModel.addToCart(cartItem.copy(quantity = newQuantity))
                                        },
                                        onDeleteItem = {
                                            cartViewModel.deleteCartItem(cartItem)
                                            Toast.makeText(context, "${cartItem.foodName} removed", Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }

                                !cartItem.cakeName.isNullOrBlank() -> {
                                    CartCakeItemRow(
                                        cartItem = cartItem,
                                        onUpdateCakeQuantity = { newQuantity ->
                                            cartViewModel.addToCart(cartItem.copy(cakeQuantity = newQuantity))
                                        },
                                        onDeleteItem = {
                                            cartViewModel.deleteCartItem(cartItem)
                                            Toast.makeText(context, "${cartItem.cakeName} removed", Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }

                                !cartItem.hotDrinkName.isNullOrBlank() -> {
                                    CartHotDrinkItemRow(
                                        cartItem = cartItem,
                                        onUpdateHotDrinkQuantity = { newQuantity ->
                                            cartViewModel.addToCart(cartItem.copy(hotDrinkQuantity = newQuantity))
                                        },
                                        onDeleteItem = {
                                            cartViewModel.deleteCartItem(cartItem)
                                            Toast.makeText(context, "${cartItem.hotDrinkName} removed", Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }

                                !cartItem.softDrinkName.isNullOrBlank() -> {
                                    CartSoftDrinkItemRow(
                                        cartItem = cartItem,
                                        onUpdateSoftDrinkQuantity = { newQuantity ->
                                            cartViewModel.addToCart(cartItem.copy(softDrinkQuantity = newQuantity))
                                        },
                                        onDeleteItem = {
                                            cartViewModel.deleteCartItem(cartItem)
                                            Toast.makeText(context, "${cartItem.softDrinkName} removed", Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }

                                !cartItem.breadName.isNullOrBlank() -> {
                                    CartBreadItemRow(
                                        cartItem = cartItem,
                                        onUpdateBreadQuantity = { newQuantity ->
                                            cartViewModel.addToCart(cartItem.copy(breadQuantity = newQuantity))
                                        },
                                        onDeleteItem = {
                                            cartViewModel.deleteCartItem(cartItem)
                                            Toast.makeText(context, "${cartItem.breadName} removed", Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }

                                !cartItem.juiceName.isNullOrBlank() -> {
                                    CartJuiceItemRow(
                                        cartItem = cartItem,
                                        onUpdateJuiceQuantity = { newQuantity ->
                                            cartViewModel.addToCart(cartItem.copy(juiceQuantity = newQuantity))
                                        },
                                        onDeleteItem = {
                                            cartViewModel.deleteCartItem(cartItem)
                                            Toast.makeText(
                                                context, "${cartItem.juiceName} removed", Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                SelectWaiter(
                    waiterViewModel = waiterViewModel,
                    onWaiterSelected = { waiter ->
                        selectedWaiter = waiter
                        isButtonClicked = false
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Enable the button only if a waiter is selected
                isButtonEnabled = selectedWaiter != null && !isButtonClicked

                Button(
                    onClick = {
                        if (!isButtonEnabled)
                            return@Button
                        isButtonClicked = true

                        if (selectedWaiter == null) {
                            isButtonClicked = false
                            return@Button
                        }

                        // Get the current date and time
                        val date = Date()
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        val orderDate = dateFormat.format(date)

                        // Prepare JSON data for the HTTP request
                        val jsonArray = JSONArray()
                        cartItems.forEach { cartItem ->
                            // Handle food items
                            if (!cartItem.foodName.isNullOrEmpty()) {
                                val orderJson = JSONObject().apply {
                                    put("waiterName", selectedWaiter?.waiterName ?: "")
                                    put("itemType", "food")
                                    put("itemName", cartItem.foodName)
                                    put("quantity", cartItem.quantity)
                                    put("totalPrice", cartItem.foodPrice * cartItem.quantity)
                                    put("orderDate", orderDate)
                                }
                                jsonArray.put(orderJson)
                            }

                            // Handle cake items
                            if (!cartItem.cakeName.isNullOrEmpty()) {
                                val orderJson = JSONObject().apply {
                                    put("waiterName", selectedWaiter?.waiterName ?: "")
                                    put("itemType", "cake")
                                    put("itemName", cartItem.cakeName)
                                    put("quantity", cartItem.cakeQuantity)
                                    put("totalPrice", cartItem.cakePrice * cartItem.cakeQuantity)
                                    put("orderDate", orderDate)
                                }
                                jsonArray.put(orderJson)
                            }

                            // Handle hot drink items
                            if (!cartItem.hotDrinkName.isNullOrEmpty()) {
                                val orderJson = JSONObject().apply {
                                    put("waiterName", selectedWaiter?.waiterName ?: "")
                                    put("itemType", "hotDrink")
                                    put("itemName", cartItem.hotDrinkName)
                                    put("quantity", cartItem.hotDrinkQuantity)
                                    put("totalPrice", cartItem.hotDrinkPrice * cartItem.hotDrinkQuantity)
                                    put("orderDate", orderDate)
                                }
                                jsonArray.put(orderJson)
                            }

                            // Handle soft drink items
                            if (!cartItem.softDrinkName.isNullOrEmpty()) {
                                val orderJson = JSONObject().apply {
                                    put("waiterName", selectedWaiter?.waiterName ?: "")
                                    put("itemType", "softDrink")
                                    put("itemName", cartItem.softDrinkName)
                                    put("quantity", cartItem.softDrinkQuantity)
                                    put("totalPrice", cartItem.softDrinkPrice * cartItem.softDrinkQuantity)
                                    put("orderDate", orderDate)
                                }
                                jsonArray.put(orderJson)
                            }

                            // Handle bread items
                            if (!cartItem.breadName.isNullOrEmpty()) {
                                val orderJson = JSONObject().apply {
                                    put("waiterName", selectedWaiter?.waiterName ?: "")
                                    put("itemType", "bread")
                                    put("itemName", cartItem.breadName)
                                    put("quantity", cartItem.breadQuantity)
                                    put("totalPrice", cartItem.breadPrice * cartItem.breadQuantity)
                                    put("orderDate", orderDate)
                                }
                                jsonArray.put(orderJson)
                            }

                            // Handle juice items
                            if (!cartItem.juiceName.isNullOrEmpty()) {
                                val orderJson = JSONObject().apply {
                                    put("waiterName", selectedWaiter?.waiterName ?: "")
                                    put("itemType", "juice")
                                    put("itemName", cartItem.juiceName)
                                    put("quantity", cartItem.juiceQuantity)
                                    put("totalPrice", cartItem.juicePrice * cartItem.juiceQuantity)
                                    put("orderDate", orderDate)
                                }
                                jsonArray.put(orderJson)
                            }
                        }

                        // Send the order to the remote server
                        val receiverIp = "192.168.0.3"
                        val url = "http://$receiverIp:8080"
                        val client = OkHttpClient()
                        val requestBody = jsonArray.toString().toRequestBody("application/json".toMediaTypeOrNull())

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val request = Request.Builder()
                                    .url(url)
                                    .post(requestBody)
                                    .build()

                                val response = client.newCall(request).execute()
                                if (response.isSuccessful) {
                                    // If the server request is successful, save the order locally
                                    cartItems.forEach { cartItem ->
                                        /*
                                        // Handle food items
                                        if (!cartItem.foodName.isNullOrEmpty()) {
                                            val order = Order(
                                                waiterName = selectedWaiter?.waiterName ?: "",
                                                foodName = cartItem.foodName,
                                                quantity = cartItem.quantity,
                                                totalPrice = cartItem.foodPrice * cartItem.quantity,
                                                orderDate = orderDate
                                            )
                                            orderViewModel.insertOrder(order)
                                        }

                                        // Handle cake items
                                        if (!cartItem.cakeName.isNullOrEmpty()) {
                                            val otherOrder = OtherOrder(
                                                waiterName = selectedWaiter?.waiterName ?: "",
                                                itemName = cartItem.cakeName,
                                                itemType = "cake",
                                                quantity = cartItem.cakeQuantity,
                                                totalPrice = cartItem.cakePrice * cartItem.cakeQuantity,
                                                orderDate = orderDate
                                            )
                                            otherOrderViewModel.insertOtherOrder(otherOrder)
                                        }

                                        // Handle hot drink items
                                        if (!cartItem.hotDrinkName.isNullOrEmpty()) {
                                            val otherOrder = OtherOrder(
                                                waiterName = selectedWaiter?.waiterName ?: "",
                                                itemName = cartItem.hotDrinkName,
                                                itemType = "hotDrink",
                                                quantity = cartItem.hotDrinkQuantity,
                                                totalPrice = cartItem.hotDrinkPrice * cartItem.hotDrinkQuantity,
                                                orderDate = orderDate
                                            )
                                            otherOrderViewModel.insertOtherOrder(otherOrder)
                                        }

                                        // Handle soft drink items
                                        if (!cartItem.softDrinkName.isNullOrEmpty()) {
                                            val otherOrder = OtherOrder(
                                                waiterName = selectedWaiter?.waiterName ?: "",
                                                itemName = cartItem.softDrinkName,
                                                itemType = "softDrink",
                                                quantity = cartItem.softDrinkQuantity,
                                                totalPrice = cartItem.softDrinkPrice * cartItem.softDrinkQuantity,
                                                orderDate = orderDate
                                            )
                                            otherOrderViewModel.insertOtherOrder(otherOrder)
                                        }

                                        // Handle bread items
                                        if (!cartItem.breadName.isNullOrEmpty()) {
                                            val otherOrder = OtherOrder(
                                                waiterName = selectedWaiter?.waiterName ?: "",
                                                itemName = cartItem.breadName,
                                                itemType = "bread",
                                                quantity = cartItem.breadQuantity,
                                                totalPrice = cartItem.breadPrice * cartItem.breadQuantity,
                                                orderDate = orderDate
                                            )
                                            otherOrderViewModel.insertOtherOrder(otherOrder)
                                        }
                                         */

                                        // Handle juice items
                                        if (!cartItem.juiceName.isNullOrEmpty()) {
                                            val otherOrder = OtherOrder(
                                                waiterName = selectedWaiter?.waiterName ?: "",
                                                itemName = cartItem.juiceName,
                                                itemType = "juice",
                                                quantity = cartItem.juiceQuantity,
                                                totalPrice = cartItem.juicePrice * cartItem.juiceQuantity,
                                                orderDate = orderDate
                                            )
                                            otherOrderViewModel.insertOtherOrder(otherOrder)
                                        }
                                    }

                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Order sent successfully!", Toast.LENGTH_SHORT).show()
                                        cartViewModel.clearCart()
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Failed to send order", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            } finally {
                                isButtonClicked = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Teal),
                    shape = RoundedCornerShape(12.dp),
                    enabled = isButtonEnabled
                ) {
                    Text("Send Order")
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: Cart,
    onUpdateQuantity: (Int) -> Unit,
    onDeleteItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    var quantity by remember { mutableIntStateOf(cartItem.quantity) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.foodName.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${"%.2f".format(cartItem.foodPrice)}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TealDark
                    )
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                modifier = Modifier
                    .width(100.dp)
                    .height(36.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (quantity > 1) {
                                quantity--
                                onUpdateQuantity(quantity)
                            }
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease quantity",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    IconButton(
                        onClick = {
                            quantity++
                            onUpdateQuantity(quantity)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            IconButton(
                onClick = onDeleteItem,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete item",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CartCakeItemRow(
    cartItem: Cart,
    onUpdateCakeQuantity: (Int) -> Unit,
    onDeleteItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    var cakeQuantity by remember { mutableIntStateOf(cartItem.cakeQuantity) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.cakeName.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${"%.2f".format(cartItem.cakePrice)}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TealDark
                    )
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                modifier = Modifier
                    .width(100.dp)
                    .height(36.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (cakeQuantity > 1) {
                                cakeQuantity--
                                onUpdateCakeQuantity(cakeQuantity)
                            }
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease quantity",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = cakeQuantity.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    IconButton(
                        onClick = {
                            cakeQuantity++
                            onUpdateCakeQuantity(cakeQuantity)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            IconButton(
                onClick = onDeleteItem,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete item",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CartHotDrinkItemRow(
    cartItem: Cart,
    onUpdateHotDrinkQuantity: (Int) -> Unit,
    onDeleteItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    var hotDrinkQuantity by remember { mutableIntStateOf(cartItem.hotDrinkQuantity) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.hotDrinkName.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${"%.2f".format(cartItem.hotDrinkPrice)}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TealDark
                    )
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                modifier = Modifier
                    .width(100.dp)
                    .height(36.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (hotDrinkQuantity > 1) {
                                hotDrinkQuantity--
                                onUpdateHotDrinkQuantity(hotDrinkQuantity)
                            }
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease quantity",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = hotDrinkQuantity.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    IconButton(
                        onClick = {
                            hotDrinkQuantity++
                            onUpdateHotDrinkQuantity(hotDrinkQuantity)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            IconButton(
                onClick = onDeleteItem,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete item",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CartSoftDrinkItemRow(
    cartItem: Cart,
    onUpdateSoftDrinkQuantity: (Int) -> Unit,
    onDeleteItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    var softDrinkQuantity by remember { mutableIntStateOf(cartItem.softDrinkQuantity) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.softDrinkName.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${"%.2f".format(cartItem.softDrinkPrice)}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TealDark
                    )
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                modifier = Modifier
                    .width(100.dp)
                    .height(36.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (softDrinkQuantity > 1) {
                                softDrinkQuantity--
                                onUpdateSoftDrinkQuantity(softDrinkQuantity)
                            }
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease quantity",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = softDrinkQuantity.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    IconButton(
                        onClick = {
                            softDrinkQuantity++
                            onUpdateSoftDrinkQuantity(softDrinkQuantity)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            IconButton(
                onClick = onDeleteItem,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete item",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CartBreadItemRow(
    cartItem: Cart,
    onUpdateBreadQuantity: (Int) -> Unit,
    onDeleteItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    var breadQuantity by remember { mutableIntStateOf(cartItem.breadQuantity) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.breadName.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${"%.2f".format(cartItem.breadPrice)}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TealDark
                    )
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                modifier = Modifier
                    .width(100.dp)
                    .height(36.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (breadQuantity > 1) {
                                breadQuantity--
                                onUpdateBreadQuantity(breadQuantity)
                            }
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease quantity",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = breadQuantity.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    IconButton(
                        onClick = {
                            breadQuantity++
                            onUpdateBreadQuantity(breadQuantity)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            IconButton(
                onClick = onDeleteItem,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete item",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CartJuiceItemRow(
    cartItem: Cart,
    onUpdateJuiceQuantity: (Int) -> Unit,
    onDeleteItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    var juiceQuantity by remember { mutableIntStateOf(cartItem.juiceQuantity) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.juiceName.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$${"%.2f".format(cartItem.juicePrice)}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TealDark
                    )
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                modifier = Modifier
                    .width(100.dp)
                    .height(36.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (juiceQuantity > 1) {
                                juiceQuantity--
                                onUpdateJuiceQuantity(juiceQuantity)
                            }
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease quantity",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = juiceQuantity.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    IconButton(
                        onClick = {
                            juiceQuantity++
                            onUpdateJuiceQuantity(juiceQuantity)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            IconButton(
                onClick = onDeleteItem,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete item",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectWaiter(
    waiterViewModel: WaiterViewModel,
    onWaiterSelected: (Waiter) -> Unit,
    modifier: Modifier = Modifier
) {
    val waiters by waiterViewModel.allWaiters.collectAsState(initial = emptyList())
    var expanded by remember { mutableStateOf(false) }
    var selectedWaiter by remember { mutableStateOf<Waiter?>(null) }

    Column(modifier = modifier.padding(16.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedWaiter?.waiterName ?: "",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(
                        type = MenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    ),
                label = { Text("Select Waiter") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.outline
                ),
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                if (waiters.isEmpty()) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "No waiters available",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = { expanded = false }
                    )
                } else {
                    waiters.forEach { waiter ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = waiter.waiterName,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                selectedWaiter = waiter
                                expanded = false
                                onWaiterSelected(waiter)
                            },
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}