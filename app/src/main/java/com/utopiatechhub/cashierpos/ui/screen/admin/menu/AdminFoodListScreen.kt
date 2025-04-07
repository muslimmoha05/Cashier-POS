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
import com.utopiatechhub.cashierpos.data.FoodWithCategory
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.repository.CartRepository
import com.utopiatechhub.cashierpos.repository.FoodRepository
import com.utopiatechhub.cashierpos.ui.theme.Teal
import com.utopiatechhub.cashierpos.viewmodel.CartViewModel
import com.utopiatechhub.cashierpos.viewmodel.FoodViewModel
import com.utopiatechhub.cashierpos.viewmodel.factory.CartViewModelFactory
import com.utopiatechhub.cashierpos.viewmodel.factory.FoodViewModelFactory
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminFoodListScreen(foodCategoryId: Long) {
    var searchQuery by remember { mutableStateOf("") }

    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context.applicationContext)
    val foodRepository = FoodRepository(database.foodDao())
    val cartRepository = CartRepository(database.cartDao())
    val foodViewModel: FoodViewModel = viewModel(
        factory = FoodViewModelFactory(foodRepository)
    )
    val cartViewModel: CartViewModel = viewModel(
        factory = CartViewModelFactory(cartRepository)
    )

    LaunchedEffect(foodCategoryId) {
        foodViewModel.fetchFoodsByCategory(foodCategoryId)
    }

    // Success message states
    var showSuccessMessage by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var messageColor by remember { mutableStateOf(Teal.copy(alpha = 0.9f)) }

    // Show success message with a slide-in effect
    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            delay(2000)
            showSuccessMessage = false
        }
    }

    // Collect the foods by category
    val foodsByCategoryId by foodViewModel.categoryFoods.collectAsState(initial = emptyList())
    val foodErrorMessage by foodViewModel.errorMessage.collectAsState(initial = null)

    val filteredFoods = if (searchQuery.isEmpty()) foodsByCategoryId else {
        foodsByCategoryId.filter { it.foodName.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (foodErrorMessage != null) {
                    Text(
                        text = foodErrorMessage ?: "Error loading foods",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else if (foodsByCategoryId.isEmpty()) {
                    Text(
                        text = "No foods available.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn {
                        items(filteredFoods, key = { it.id }) { foodWithCategory ->
                            FoodItem(
                                foodWithCategory = foodWithCategory,
                                cartViewModel = cartViewModel,
                                onSuccessMessage = { message, isAlreadyInCart ->
                                    successMessage = message
                                    messageColor = if (isAlreadyInCart) Color(0xFFFFC107) else Teal.copy(alpha = 0.9f)
                                    showSuccessMessage = true
                                }
                            )
                        }
                    }
                }
            }

            // Success Message - Positioned on top
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
fun FoodItem(
    foodWithCategory: FoodWithCategory,
    cartViewModel: CartViewModel,
    onSuccessMessage: (String, Boolean) -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = foodWithCategory.foodName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "${foodWithCategory.foodPrice}", color = Color.Gray, fontSize = 14.sp)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (quantity > 1) quantity-- }) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease Quantity",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(text = quantity.toString(), style = MaterialTheme.typography.bodyMedium)
                IconButton(onClick = { quantity++ }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase Quantity",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            IconButton(onClick = {
                val isAlreadyInCart = cartItems.any { cartItem -> cartItem.foodName == foodWithCategory.foodName }

                if (isAlreadyInCart) {
                    onSuccessMessage("${foodWithCategory.foodName} already added to cart", true)
                } else {
                    val cartItem = Cart(
                        foodName = foodWithCategory.foodName,
                        quantity = quantity,
                        foodPrice = foodWithCategory.foodPrice,
                        hotDrinkName = "",
                        hotDrinkPrice = 0.0,
                        hotDrinkQuantity = 0,
                        softDrinkName = "",
                        softDrinkPrice = 0.0,
                        softDrinkQuantity = 0,
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
                    onSuccessMessage("${foodWithCategory.foodName} added to cart successfully", false)
                }
            }) {
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
