package com.utopiatechhub.cashierpos.ui.screen.admin.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utopiatechhub.cashierpos.data.Cart
import com.utopiatechhub.cashierpos.data.Juice
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.repository.CartRepository
import com.utopiatechhub.cashierpos.repository.JuiceRepository
import com.utopiatechhub.cashierpos.ui.theme.Teal
import com.utopiatechhub.cashierpos.viewmodel.CartViewModel
import com.utopiatechhub.cashierpos.viewmodel.JuiceViewModel
import com.utopiatechhub.cashierpos.viewmodel.factory.CartViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.JuiceViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun AdminJuiceList() {
    var searchQuery by remember { mutableStateOf("") }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var messageColor by remember { mutableStateOf(Teal.copy(alpha = 0.9f)) }

    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context.applicationContext)
    val cartRepository = CartRepository(database.cartDao())
    val juiceRepository = JuiceRepository(database.juiceDao())
    val juiceViewModel: JuiceViewModel = viewModel(
        factory = JuiceViewModelFactory(juiceRepository)
    )
    val cartViewModel: CartViewModel = viewModel(
        factory = CartViewModelFactory(cartRepository)
    )
    val juices by juiceViewModel.allJuices.collectAsState(initial = emptyList())
    val errorMessage by juiceViewModel.errorMessage.collectAsState(initial = null)
    val filteredJuices = if (searchQuery.isEmpty()) juices else {
        juices.filter { it.juiceName.contains(searchQuery, ignoreCase = true) }
    }

    LaunchedEffect(Unit) {
        juiceViewModel.fetchAllJuices()
    }
    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            delay(2000)
            showSuccessMessage = false
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "Error loading juices",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else if (juices.isEmpty()) {
                    Text(
                        text = "No juices are available.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredJuices) { juice ->
                            JuiceItem(
                                juice = juice,
                                cartViewModel = cartViewModel,
                                onSuccessMessage = { message, isAlreadyInCart ->
                                    successMessage = message
                                    messageColor =
                                        if (isAlreadyInCart) Color(0xFFFFC107) else Teal.copy(alpha = 0.9f)
                                    showSuccessMessage = true
                                }
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = showSuccessMessage,
                enter = slideInHorizontally { fullWidth -> fullWidth } + fadeIn(),
                exit = slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp)
            ) {
                Surface(
                    color = Teal.copy(alpha = 0.9f),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = successMessage,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun JuiceItem(
    juice: Juice,
    cartViewModel: CartViewModel,
    onSuccessMessage: (String, Boolean) -> Unit
) {
    var juiceQuantity by remember { mutableIntStateOf(1) }
    val cartItems by cartViewModel.cartItems.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = juice.juiceName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "${juice.juicePrice}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { if (juiceQuantity > 1) juiceQuantity-- }, // Fixed the condition
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease Quantity"
                        )
                    }
                    Text(
                        text = juiceQuantity.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    IconButton(
                        onClick = { juiceQuantity++ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase Quantity"
                        )
                    }
                }

                IconButton(
                    onClick = {
                        val isAlreadyInCart =
                            cartItems.any { cartItem -> cartItem.juiceName == juice.juiceName } // Changed from breadName to juiceName

                        if (isAlreadyInCart) {
                            onSuccessMessage("${juice.juiceName} already added to cart", true)
                        } else {
                            val cartItem = Cart(
                                juiceName = juice.juiceName,
                                juicePrice = juice.juicePrice,
                                juiceQuantity = juiceQuantity,
                                softDrinkName = "",
                                softDrinkQuantity = 0,
                                softDrinkPrice = 0.0,
                                foodName = "",
                                foodPrice = 0.0,
                                quantity = 0,
                                hotDrinkName = "",
                                hotDrinkPrice = 0.0,
                                hotDrinkQuantity = 0,
                                breadName = "",
                                breadPrice = 0.0,
                                breadQuantity = 0,
                                cakeName = "",
                                cakePrice = 0.0,
                                cakeQuantity = 0
                            )
                            cartViewModel.addToCart(cartItem)
                            onSuccessMessage(
                                "${juice.juiceName} added to cart successfully",
                                false
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add to Cart",
                        modifier = Modifier.size(24.dp),
                        tint = Teal
                    )
                }
            }
        }
    }
}