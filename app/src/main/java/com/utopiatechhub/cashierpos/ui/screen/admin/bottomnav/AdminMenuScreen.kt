package com.utopiatechhub.cashierpos.ui.screen.admin.bottomnav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.utopiatechhub.cashierpos.ui.screen.admin.menu.AdminBreadList
import com.utopiatechhub.cashierpos.ui.screen.admin.menu.AdminCakeListScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.menu.AdminFoodCategoryScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.menu.AdminFoodListScreen
import com.utopiatechhub.cashierpos.ui.screen.admin.menu.AdminHotDrinkList
import com.utopiatechhub.cashierpos.ui.screen.admin.menu.AdminJuiceList
import com.utopiatechhub.cashierpos.ui.screen.admin.menu.AdminPackedFoodList
import com.utopiatechhub.cashierpos.ui.screen.admin.menu.AdminSoftDrinkList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMenuScreen() {
    val navController = rememberNavController()
    val categories = listOf(
        "ዳቦ እና አሳንቡሳ", "ኬክ", "የታሸጉ ምግቦች", "ምግብ", "ትኩስ መጠጥ", "ውሃ እና ለስላሳ መጠጥ", "ጁስ"
    )
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Horizontal scrollable category menu
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories.size) { index ->
                    val category = categories[index]
                    CategoryChip(
                        category = category,
                        isSelected = category == selectedCategory,
                        onClick = {
                            selectedCategory = category
                            navigateToCategory(navController, category)
                        }
                    )
                }
            }

            // Navigation content
            NavHost(
                navController = navController,
                startDestination = "emptyScreen",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("emptyScreen") {
                    Text( text = "Select a category to view items", modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center, style = MaterialTheme.typography.titleMedium) }
                composable("foodCategoryScreen") { AdminFoodCategoryScreen(navController) }
                composable("food_list/{foodCategoryId}") { backStackEntry ->
                    val foodCategoryId = backStackEntry.arguments?.getString("foodCategoryId")?.toLongOrNull()
                    if (foodCategoryId != null) {
                        AdminFoodListScreen(foodCategoryId)
                    } else {
                        // Handle invalid categoryId or show an error screen where categoryId null
                    }
                }
                composable("hotDrinkListScreen") { AdminHotDrinkList() }
                composable("softDrinkListScreen") { AdminSoftDrinkList() }
                composable("breadListScreen") { AdminBreadList() }
                composable("cakeListScreen") { AdminCakeListScreen() }
                composable("juiceListScreen") { AdminJuiceList() }
                composable("packedFoodListScreen") { AdminPackedFoodList() }
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF00796B) else Color.LightGray,
            contentColor = Color.White
        ),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(category, fontWeight = FontWeight.Bold)
    }
}

fun navigateToCategory(navController: NavHostController, category: String) {
    when (category) {
        "የታሸጉ ምግቦች" -> navController.navigate("packedFoodListScreen")
        "ምግብ" -> navController.navigate("foodCategoryScreen")
        "ትኩስ መጠጥ" -> navController.navigate("hotDrinkListScreen")
        "ውሃ እና ለስላሳ መጠጥ" -> navController.navigate("softDrinkListScreen")
        "ዳቦ እና አሳንቡሳ" -> navController.navigate("breadListScreen")
        "ኬክ" -> navController.navigate("cakeListScreen")
        "ጁስ" -> navController.navigate("juiceListScreen")
    }
}
