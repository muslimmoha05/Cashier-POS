package com.utopiatechhub.cashierpos.ui.screen.client.menu

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.utopiatechhub.cashierpos.R
import com.utopiatechhub.cashierpos.authentication.TenantDataStore
import com.utopiatechhub.cashierpos.data.Cart
import com.utopiatechhub.cashierpos.data.OtherOrder
import com.utopiatechhub.cashierpos.data.SoftDrink
import com.utopiatechhub.cashierpos.data.Waiter
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.repository.CartRepository
import com.utopiatechhub.cashierpos.repository.OrderRepository
import com.utopiatechhub.cashierpos.repository.OtherOrderRepository
import com.utopiatechhub.cashierpos.repository.SoftDrinkRepository
import com.utopiatechhub.cashierpos.repository.WaiterRepository
import com.utopiatechhub.cashierpos.ui.screen.client.bottomandsidenav.SelectWaiter
import com.utopiatechhub.cashierpos.ui.theme.Teal
import com.utopiatechhub.cashierpos.ui.theme.TealDark
import com.utopiatechhub.cashierpos.viewmodel.CartViewModel
import com.utopiatechhub.cashierpos.viewmodel.OrderViewModel
import com.utopiatechhub.cashierpos.viewmodel.OtherOrderViewModel
import com.utopiatechhub.cashierpos.viewmodel.SoftDrinkViewModel
import com.utopiatechhub.cashierpos.viewmodel.WaiterViewModel
import com.utopiatechhub.cashierpos.viewmodel.factory.CartViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.OrderViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.OtherOrderViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.SoftDrinkViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.WaiterViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientSoftDrinkList(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedWaiter by remember { mutableStateOf<Waiter?>(null) }
    var isButtonClicked by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val database = AppDatabase.getDatabase(context.applicationContext)

    val waiterRepository = WaiterRepository(database.waiterDao())
    val orderRepository = OrderRepository(database.orderDao())
    val otherOrderRepository = OtherOrderRepository(database.otherOrderDao())
    val tenantDataStore = TenantDataStore(context)

    val softDrinkRepository = SoftDrinkRepository(database.softDrinkDao())
    val cartRepository = CartRepository(database.cartDao())
    val softDrinkViewModel: SoftDrinkViewModel = viewModel(factory = SoftDrinkViewModelFactory(softDrinkRepository))
    val cartViewModel: CartViewModel = viewModel(factory = CartViewModelFactory(cartRepository))
    val waiterViewModel: WaiterViewModel = viewModel(factory = WaiterViewModelFactory(waiterRepository, tenantDataStore))
    val orderViewModel: OrderViewModel = viewModel(factory = OrderViewModelFactory(orderRepository))
    val otherOrderViewModel: OtherOrderViewModel = viewModel(factory = OtherOrderViewModelFactory(otherOrderRepository))

    val softDrinks by softDrinkViewModel.allSoftDrinks.collectAsState(initial = emptyList())
    val errorMessage by softDrinkViewModel.errorMessage.collectAsState(initial = null)
    val cartItems by cartViewModel.cartItems.collectAsState()

    val filteredSoftDrinks = if (searchQuery.isEmpty()) softDrinks else {
        softDrinks.filter { it.softDrinkName.contains(searchQuery, ignoreCase = true) }
    }

    LaunchedEffect(Unit) {
        softDrinkViewModel.fetchAllSoftDrinks()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Surface(
                shadowElevation = 8.dp,
                tonalElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                TopAppBar(
                    title = {
                        if (isSearchActive) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                label = { Text("Search soft drinks...") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Clear search",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Teal,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                        } else {
                            Text(
                                "Soft Drink Menu",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        if (!isSearchActive) {
                            IconButton(
                                onClick = { isSearchActive = true },
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    action = {
                        if (snackbarData.visuals.actionLabel != null) {
                            TextButton(
                                onClick = { scope.launch { snackbarHostState.currentSnackbarData?.dismiss() } },
                                colors = ButtonDefaults.textButtonColors(contentColor = Teal)
                            ) {
                                Text(snackbarData.visuals.actionLabel!!)
                            }
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Text(snackbarData.visuals.message)
                    if (snackbarData.visuals.message == "Please select a waiter") {
                        Column(
                            modifier = Modifier.padding(top = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SelectWaiter(
                                waiterViewModel = waiterViewModel,
                                onWaiterSelected = { waiter ->
                                    selectedWaiter = waiter
                                }
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 8.dp),
                    color = Teal,
                    tonalElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable(
                                enabled = !isButtonClicked, // Disable clicking when processing
                                onClick = {
                                    if (selectedWaiter == null) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "Please select a waiter",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                        return@clickable
                                    }

                                    if (isButtonClicked) return@clickable // Prevent multiple submissions

                                    isButtonClicked = true
                                    val waiterName = selectedWaiter!!.waiterName
                                    val date = Date()
                                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                    val orderDate = dateFormat.format(date)

                                    val jsonArray = JSONArray()
                                    cartItems.forEach { cartItem ->
                                        fun createOrderJson(itemType: String, itemName: String?, quantity: Int, price: Double): JSONObject? {
                                            return itemName?.takeIf { it.isNotEmpty() }?.let {
                                                JSONObject().apply {
                                                    put("waiterName", waiterName)
                                                    put("itemType", itemType)
                                                    put("itemName", itemName)
                                                    put("quantity", quantity)
                                                    put("totalPrice", price * quantity)
                                                    put("orderDate", orderDate)
                                                }
                                            }
                                        }

                                        listOfNotNull(
                                            createOrderJson("food", cartItem.foodName, cartItem.quantity, cartItem.foodPrice),
                                            createOrderJson("cake", cartItem.cakeName, cartItem.cakeQuantity, cartItem.cakePrice),
                                            createOrderJson("hotDrink", cartItem.hotDrinkName, cartItem.hotDrinkQuantity, cartItem.hotDrinkPrice),
                                            createOrderJson("softDrink", cartItem.softDrinkName, cartItem.softDrinkQuantity, cartItem.softDrinkPrice),
                                            createOrderJson("bread", cartItem.breadName, cartItem.breadQuantity, cartItem.breadPrice),
                                            createOrderJson("juice", cartItem.juiceName, cartItem.juiceQuantity, cartItem.juicePrice)
                                        ).forEach { jsonArray.put(it) }
                                    }

                                    val receiverIp = "192.168.0.3"
                                    val url = "http://$receiverIp:8080"
                                    val client = OkHttpClient()
                                    val requestBody = jsonArray.toString().toRequestBody("application/json".toMediaTypeOrNull())

                                    scope.launch(Dispatchers.IO) {
                                        try {
                                            val request = Request.Builder().url(url).post(requestBody).build()
                                            val response = client.newCall(request).execute()
                                            if (response.isSuccessful) {
                                                cartItems.forEach { cartItem ->
                                                    cartItem.juiceName?.takeIf { it.isNotEmpty() }?.let { juiceName ->
                                                        val otherOrder = OtherOrder(
                                                            waiterName = waiterName,
                                                            itemName = juiceName,
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
                                                    navController.popBackStack()
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
                                            withContext(Dispatchers.Main) {
                                                isButtonClicked = false
                                            }
                                        }
                                    }
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val totalItems = cartItems.sumOf {
                            it.quantity + it.hotDrinkQuantity + it.softDrinkQuantity +
                                    it.breadQuantity + it.juiceQuantity + it.cakeQuantity
                        }
                        val totalPrice = cartItems.sumOf {
                            (it.foodPrice * it.quantity) + (it.hotDrinkPrice * it.hotDrinkQuantity) +
                                    (it.softDrinkPrice * it.softDrinkQuantity) + (it.breadPrice * it.breadQuantity) +
                                    (it.juicePrice * it.juiceQuantity) + (it.cakePrice * it.cakeQuantity)
                        }
                        Text(
                            "$totalItems items",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Send Order",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            selectedWaiter?.let { waiter ->
                                Text(
                                    "(${waiter.waiterName})",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Normal,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                )
                            }
                            if (isButtonClicked) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                        Text(
                            "$${"%.2f".format(totalPrice)}",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            when {
                errorMessage != null -> {
                    ErrorState(
                        message = errorMessage ?: "Error loading soft drinks",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    )
                }

                softDrinks.isEmpty() -> {
                    EmptyState(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    )
                }

                filteredSoftDrinks.isEmpty() -> {
                    NoResultsState(
                        searchQuery = searchQuery,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = filteredSoftDrinks,
                            key = { it.id }
                        ) { softDrink ->
                            SoftDrinkItem(
                                softDrink = softDrink,
                                cartViewModel = cartViewModel,
                                isInCart = cartItems.any { it.softDrinkName == softDrink.softDrinkName },
                                modifier = Modifier.animateItem(
                                    fadeInSpec = null, fadeOutSpec = null, placementSpec = tween(durationMillis = 250)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SoftDrinkItem(
    softDrink: SoftDrink,
    cartViewModel: CartViewModel,
    isInCart: Boolean,
    modifier: Modifier = Modifier
) {
    var quantity by remember { mutableIntStateOf(1) }
    var showAddedAnimation by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = softDrink.softDrinkName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$${"%.2f".format(softDrink.softDrinkPrice)}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TealDark
                    )
                )

                AnimatedVisibility(
                    visible = showAddedAnimation,
                    enter = slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = tween(durationMillis = 300)
                    ) + fadeIn(animationSpec = tween(durationMillis = 300)),
                    exit = slideOutVertically(
                        targetOffsetY = { -it },
                        animationSpec = tween(durationMillis = 300)
                    ) + fadeOut(animationSpec = tween(durationMillis = 300))
                ) {
                    Text(
                        text = "${softDrink.softDrinkName} added to cart",
                        color = Teal,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
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
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = { quantity++ },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase quantity",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (!isInCart) {
                            showAddedAnimation = true

                            val cartItem = Cart(
                                softDrinkName = softDrink.softDrinkName,
                                softDrinkQuantity = quantity,
                                softDrinkPrice = softDrink.softDrinkPrice,
                                foodName = "",
                                foodPrice = 0.0,
                                quantity = 0,
                                hotDrinkName = "",
                                hotDrinkPrice = 0.0,
                                hotDrinkQuantity = 0,
                                breadName = "",
                                breadPrice = 0.0,
                                breadQuantity = 0,
                                juiceName = "",
                                juicePrice = 0.0,
                                juiceQuantity = 0,
                                cakeName = "",
                                cakePrice = 0.0,
                                cakeQuantity = 0
                            )
                            cartViewModel.addToCart(cartItem)

                            CoroutineScope(Dispatchers.Main).launch {
                                delay(1000)
                                showAddedAnimation = false
                            }
                        }
                    },
                    enabled = !isInCart,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isInCart) MaterialTheme.colorScheme.surfaceVariant
                            else Teal,
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add to cart",
                        tint = if (isInCart) MaterialTheme.colorScheme.onSurfaceVariant
                        else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_error),
            contentDescription = "Error",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Something Went Wrong",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_empty_state),
            contentDescription = "Empty menu",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Soft Drink Items Available",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = "Check back later or contact staff",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun NoResultsState(
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = "No results",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Results Found",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = "No soft drink items match \"$searchQuery\"",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

