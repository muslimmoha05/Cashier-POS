package com.utopiatechhub.cashierpos.ui.screen.admin.bottomnav

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.utopiatechhub.cashierpos.authentication.TenantDataStore
import com.utopiatechhub.cashierpos.data.Cart
import com.utopiatechhub.cashierpos.data.Order
import com.utopiatechhub.cashierpos.data.OtherOrder
import com.utopiatechhub.cashierpos.data.Waiter
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.repository.CartRepository
import com.utopiatechhub.cashierpos.repository.OrderRepository
import com.utopiatechhub.cashierpos.repository.OtherOrderRepository
import com.utopiatechhub.cashierpos.repository.WaiterRepository
import com.utopiatechhub.cashierpos.ui.theme.Teal
import com.utopiatechhub.cashierpos.viewmodel.CartViewModel
import com.utopiatechhub.cashierpos.viewmodel.OrderViewModel
import com.utopiatechhub.cashierpos.viewmodel.OtherOrderViewModel
import com.utopiatechhub.cashierpos.viewmodel.WaiterViewModel
import com.utopiatechhub.cashierpos.viewmodel.factory.CartViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.OrderViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.OtherOrderViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.WaiterViewModelFactory
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCartScreen(navController: NavController) {
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
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
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
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
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
                        text = "Total Price:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
                    Text(
                        text = currencyFormat.format(veryTotalPrice),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(cartItems) { cartItem ->
                        when {
                            !cartItem.foodName.isNullOrBlank() -> {
                                AdminCartItemRow(
                                    cartItem = cartItem,
                                    onUpdateQuantity = { newQuantity ->
                                        cartViewModel.addToCart(cartItem.copy(quantity = newQuantity))
                                    },
                                    onDeleteItem = {
                                        cartViewModel.deleteCartItem(cartItem)
                                        Toast.makeText(
                                            context,
                                            "${cartItem.foodName} removed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }

                            !cartItem.cakeName.isNullOrBlank() -> {
                                AdminCartCakeItemRow(
                                    cartItem = cartItem,
                                    onUpdateCakeQuantity = { newQuantity ->
                                        cartViewModel.addToCart(cartItem.copy(cakeQuantity = newQuantity))
                                    },
                                    onDeleteItem = {
                                        cartViewModel.deleteCartItem(cartItem)
                                        Toast.makeText(
                                            context,
                                            "${cartItem.cakeName} removed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }

                            !cartItem.hotDrinkName.isNullOrBlank() -> {
                                AdminCartHotDrinkItemRow(
                                    cartItem = cartItem,
                                    onUpdateHotDrinkQuantity = { newQuantity ->
                                        cartViewModel.addToCart(cartItem.copy(hotDrinkQuantity = newQuantity))
                                    },
                                    onDeleteItem = {
                                        cartViewModel.deleteCartItem(cartItem)
                                        Toast.makeText(
                                            context,
                                            "${cartItem.hotDrinkName} removed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }

                            !cartItem.softDrinkName.isNullOrBlank() -> {
                                AdminCartSoftDrinkItemRow(
                                    cartItem = cartItem,
                                    onUpdateSoftDrinkQuantity = { newQuantity ->
                                        cartViewModel.addToCart(cartItem.copy(softDrinkQuantity = newQuantity))
                                    },
                                    onDeleteItem = {
                                        cartViewModel.deleteCartItem(cartItem)
                                        Toast.makeText(
                                            context,
                                            "${cartItem.softDrinkName} removed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }

                            !cartItem.breadName.isNullOrBlank() -> {
                                AdminCartBreadItemRow(
                                    cartItem = cartItem,
                                    onUpdateBreadQuantity = { newQuantity ->
                                        cartViewModel.addToCart(cartItem.copy(breadQuantity = newQuantity))
                                    },
                                    onDeleteItem = {
                                        cartViewModel.deleteCartItem(cartItem)
                                        Toast.makeText(
                                            context,
                                            "${cartItem.breadName} removed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }

                            !cartItem.juiceName.isNullOrBlank() -> {
                                AdminCartJuiceItemRow(
                                    cartItem = cartItem,
                                    onUpdateJuiceQuantity = { newQuantity ->
                                        cartViewModel.addToCart(cartItem.copy(juiceQuantity = newQuantity))
                                    },
                                    onDeleteItem = {
                                        cartViewModel.deleteCartItem(cartItem)
                                        Toast.makeText(
                                            context,
                                            "${cartItem.juiceName} removed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                SelectWaiter(
                    waiterViewModel = waiterViewModel,
                    onWaiterSelected = { waiter ->
                        selectedWaiter = waiter
                        isButtonClicked = false // Reset button state when new waiter is selected
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

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

                        val date = Date()
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        val orderDate = dateFormat.format(date)

                        // Iterate through cart items and create orders
                        cartItems.forEach { cartItem ->
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

                        Toast.makeText(context, "Order sent!", Toast.LENGTH_SHORT).show()
                        cartViewModel.clearCart()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Teal)
                ) {
                    Text("Send Order")
                }
            }
        }
    }
}

@Composable
fun AdminCartItemRow(
    cartItem: Cart,
    onUpdateQuantity: (Int) -> Unit,
    onDeleteItem: () -> Unit
) {
    var quantity by remember { mutableIntStateOf(cartItem.quantity) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Food Name and Price
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = cartItem.foodName.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "${cartItem.foodPrice}", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
            }

            // Quantity Update Icons
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (quantity > 1) {
                        quantity--
                        onUpdateQuantity(quantity)
                    }
                }) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease Quantity")
                }
                Text(text = quantity.toString(), style = MaterialTheme.typography.bodyMedium)
                IconButton(onClick = {
                    quantity++
                    onUpdateQuantity(quantity)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
                }
            }

            // Delete Icon
            IconButton(onClick = onDeleteItem) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Item")
            }
        }
    }
}

@Composable
fun AdminCartCakeItemRow(
    cartItem: Cart,
    onUpdateCakeQuantity: (Int) -> Unit,
    onDeleteItem: () -> Unit
) {
    var cakeQuantity by remember { mutableIntStateOf(cartItem.cakeQuantity) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.cakeName.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${cartItem.cakePrice}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (cakeQuantity > 1) {
                        cakeQuantity--
                        onUpdateCakeQuantity(cakeQuantity)
                    }
                }) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease Quantity")
                }
                Text(
                    text = cakeQuantity.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
                IconButton(onClick = {
                    cakeQuantity++
                    onUpdateCakeQuantity(cakeQuantity)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
                }
            }

            IconButton(onClick = onDeleteItem) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Item")
            }
        }
    }
}

@Composable
fun AdminCartHotDrinkItemRow(
    cartItem: Cart,
    onUpdateHotDrinkQuantity: (Int) -> Unit,
    onDeleteItem: () -> Unit
) {
    var hotDrinkQuantity by remember { mutableIntStateOf(cartItem.hotDrinkQuantity) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = cartItem.hotDrinkName.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "${cartItem.hotDrinkPrice}", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (hotDrinkQuantity > 1) {
                        hotDrinkQuantity--
                        onUpdateHotDrinkQuantity(hotDrinkQuantity)
                    }
                }) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease Quantity")
                }
                Text(text = hotDrinkQuantity.toString(), style = MaterialTheme.typography.bodyMedium)
                IconButton(onClick = {
                    hotDrinkQuantity++
                    onUpdateHotDrinkQuantity(hotDrinkQuantity)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
                }
            }

            IconButton(onClick = onDeleteItem) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Item")
            }
        }
    }
}

@Composable
fun AdminCartSoftDrinkItemRow(
    cartItem: Cart,
    onUpdateSoftDrinkQuantity: (Int) -> Unit,
    onDeleteItem: () -> Unit
) {
    var softDrinkQuantity by remember { mutableIntStateOf(cartItem.softDrinkQuantity) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = cartItem.softDrinkName.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "${cartItem.softDrinkPrice}", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (softDrinkQuantity > 1) {
                        softDrinkQuantity--
                        onUpdateSoftDrinkQuantity(softDrinkQuantity)
                    }
                }) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease Quantity")
                }
                Text(text = softDrinkQuantity.toString(), style = MaterialTheme.typography.bodyMedium)
                IconButton(onClick = {
                    softDrinkQuantity++
                    onUpdateSoftDrinkQuantity(softDrinkQuantity)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
                }
            }

            IconButton(onClick = onDeleteItem) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Item")
            }
        }
    }
}

@Composable
fun AdminCartBreadItemRow(
    cartItem: Cart,
    onUpdateBreadQuantity: (Int) -> Unit,
    onDeleteItem: () -> Unit
) {
    var breadQuantity by remember { mutableIntStateOf(cartItem.breadQuantity) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = cartItem.breadName.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "${cartItem.breadPrice}", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (breadQuantity > 1) {
                        breadQuantity--
                        onUpdateBreadQuantity(breadQuantity)
                    }
                }) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease Quantity")
                }
                Text(text = breadQuantity.toString(), style = MaterialTheme.typography.bodyMedium)
                IconButton(onClick = {
                    breadQuantity++
                    onUpdateBreadQuantity(breadQuantity)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
                }
            }

            IconButton(onClick = onDeleteItem) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Item")
            }
        }
    }
}

@Composable
fun AdminCartJuiceItemRow(
    cartItem: Cart,
    onUpdateJuiceQuantity: (Int) -> Unit,
    onDeleteItem: () -> Unit
) {
    var juiceQuantity by remember { mutableIntStateOf(cartItem.juiceQuantity) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = cartItem.juiceName.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "${cartItem.juicePrice}", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (juiceQuantity > 1) {
                        juiceQuantity--
                        onUpdateJuiceQuantity(juiceQuantity)
                    }
                }) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease Quantity")
                }
                Text(text = juiceQuantity.toString(), style = MaterialTheme.typography.bodyMedium)
                IconButton(onClick = {
                    juiceQuantity++
                    onUpdateJuiceQuantity(juiceQuantity)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
                }
            }

            IconButton(onClick = onDeleteItem) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Item")
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
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.outline
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                if (waiters.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No waiters available") },
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