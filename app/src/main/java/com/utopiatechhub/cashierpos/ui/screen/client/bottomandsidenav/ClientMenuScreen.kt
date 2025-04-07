package com.utopiatechhub.cashierpos.ui.screen.client.bottomandsidenav

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.utopiatechhub.cashierpos.ui.screen.client.menu.ClientBreadList
import com.utopiatechhub.cashierpos.ui.screen.client.menu.ClientCakeListScreen
import com.utopiatechhub.cashierpos.ui.screen.client.menu.ClientFoodCategoryScreen
import com.utopiatechhub.cashierpos.ui.screen.client.menu.ClientFoodListScreen
import com.utopiatechhub.cashierpos.ui.screen.client.menu.ClientHotDrinkList
import com.utopiatechhub.cashierpos.ui.screen.client.menu.ClientJuiceList
import com.utopiatechhub.cashierpos.ui.screen.client.menu.ClientSoftDrinkList
import com.utopiatechhub.cashierpos.ui.theme.Teal

@Composable
fun ClientMenuScreen() {
    val navController = rememberNavController()
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val categories = listOf(
        "ምግብ", "ትኩስ መጠጥ", "ዳቦ እና አሳንቡሳ", "ውሃ እና ለስላሳ መጠጥ", "ኬክ", "ጁስ"
    )

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            // Left side (10% weight)
            Column(
                modifier = Modifier
                    .weight(0.1f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                categories.forEach { category ->
                    CategoryCard(
                        category = category,
                        isSelected = category == selectedCategory,
                        onClick = {
                            selectedCategory = category
                            when (category) {
                                "ምግብ" -> navController.navigate("foodCategoryScreen")
                                "ትኩስ መጠጥ" -> navController.navigate("hotDrinkListScreen")
                                "ውሃ እና ለስላሳ መጠጥ" -> navController.navigate("softDrinkListScreen")
                                "ዳቦ እና አሳንቡሳ" -> navController.navigate("breadListScreen")
                                "ኬክ" -> navController.navigate("cakeListScreen")
                                "ጁስ" -> navController.navigate("juiceListScreen")
                                else -> { /* Do nothing for now */
                                }
                            }
                        }
                    )
                }
            }

            // Right side (90% weight)
            Column(
                modifier = Modifier
                    .weight(0.9f)
                    .padding(8.dp)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "emptyScreen",
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable("emptyScreen") {
                        Text(
                            text = "Select a category to view details",
                            modifier = Modifier.fillMaxSize(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    composable("foodCategoryScreen") {
                        ClientFoodCategoryScreen(navController)
                    }
                    composable("food_list/{foodCategoryId}") { backStackEntry ->
                        val foodCategoryId = backStackEntry.arguments?.getString("foodCategoryId")?.toLongOrNull()
                        if (foodCategoryId != null) {
                            ClientFoodListScreen(foodCategoryId, navController)
                        } else {
                            // Handle invalid categoryId or show an error screen where categoryId null
                        }
                    }
                    composable("cakeListScreen") {
                        ClientCakeListScreen(navController)
                    }
                    composable("hotDrinkListScreen") {
                        ClientHotDrinkList(navController)
                    }
                    composable("softDrinkListScreen") {
                        ClientSoftDrinkList(navController)
                    }
                    composable("breadListScreen") {
                        ClientBreadList(navController)
                    }
                    composable("juiceListScreen") {
                        ClientJuiceList(navController)
                    }
                    // Add more composable for other categories and screens as needed
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color.LightGray else Teal,
        animationSpec = tween(durationMillis = 300)
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(onClick = onClick)
            .animateContentSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = if (isSelected) Color.Black else Color.White
            )
        }
    }
}