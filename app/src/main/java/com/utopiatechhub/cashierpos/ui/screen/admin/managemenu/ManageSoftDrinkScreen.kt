package com.utopiatechhub.cashierpos.ui.screen.admin.managemenu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.utopiatechhub.cashierpos.data.SoftDrink
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.repository.SoftDrinkRepository
import com.utopiatechhub.cashierpos.ui.theme.Teal
import com.utopiatechhub.cashierpos.viewmodel.SoftDrinkViewModel
import com.utopiatechhub.cashierpos.viewmodel.factory.SoftDrinkViewModelFactory
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageSoftDrinkScreen(navController: NavController) {

    var showUpdateDialog by remember { mutableStateOf(false) }
    var selectedSoftDrinkToUpdate by remember { mutableStateOf<SoftDrink?>(null) }
    var showAddSoftDrinkDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var softDrinkName by remember { mutableStateOf("") }
    var softDrinkPrice by remember { mutableStateOf("") }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var softDrinkToDelete by remember { mutableStateOf<SoftDrink?>(null) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var showDeleteAllConfirmation by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context.applicationContext)
    val softDrinkRepository = SoftDrinkRepository(database.softDrinkDao())
    val softDrinkViewModel: SoftDrinkViewModel = viewModel(
        factory = SoftDrinkViewModelFactory(softDrinkRepository)
    )
    val allSoftDrinks by softDrinkViewModel.allSoftDrinks.collectAsState(initial = emptyList())
    val filteredSoftDrinks = if (searchQuery.isEmpty()) allSoftDrinks else {
        allSoftDrinks.filter { it.softDrinkName.contains(searchQuery, ignoreCase = true) }
    }
    val isAddButtonEnabled = softDrinkName.isNotBlank() && softDrinkPrice.isNotBlank()

    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            delay(3000)
            showSuccessMessage = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Search Soft Drink") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    searchQuery = ""
                                    isSearchActive = false
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Close Search")
                                }
                            }
                        )
                    } else {
                        Text("Manage Soft Drinks")
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddSoftDrinkDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Soft Drink")
                    }
                    IconButton(onClick = {
                        isSearchActive = !isSearchActive
                        if (!isSearchActive) searchQuery = ""
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    if (allSoftDrinks.isNotEmpty()) {
                        IconButton(onClick = { showDeleteAllConfirmation = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete All Soft Drinks")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                    items(filteredSoftDrinks) { softDrink ->
                    SoftDrinkItemCard(
                        softDrink = softDrink,
                        onEditClick = {
                            selectedSoftDrinkToUpdate = softDrink
                            showUpdateDialog = true
                        },
                        onDeleteClick = {
                            softDrinkToDelete = softDrink
                            showDeleteConfirmation = true
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = showSuccessMessage,
                enter = slideInHorizontally { fullWidth -> fullWidth } + fadeIn(),
                exit = slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut(),
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Surface(
                    color = Teal.copy(alpha = 0.9f),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = successMessage,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        if (showAddSoftDrinkDialog) {
            AlertDialog(
                onDismissRequest = { showAddSoftDrinkDialog = false },
                title = { Text("Add New Soft Drink", style = MaterialTheme.typography.headlineSmall) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = softDrinkName,
                            onValueChange = { softDrinkName = it },
                            label = { Text("Soft Drink Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = softDrinkPrice,
                            onValueChange = { softDrinkPrice = it },
                            label = { Text("Soft Drink Price") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val price = softDrinkPrice.toDoubleOrNull() ?: 0.0
                            val softDrink = SoftDrink(softDrinkName = softDrinkName, softDrinkPrice = price)
                            softDrinkViewModel.insertSoftDrink(softDrink)
                            successMessage = "$softDrinkName added successfully"
                            showSuccessMessage = true
                            softDrinkName = ""
                            softDrinkPrice = ""
                            showAddSoftDrinkDialog = false
                        },
                        enabled = isAddButtonEnabled,
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("Add")
                    }
                    Button(
                        onClick = { softDrinkViewModel.addSoftDrinksManually() },
                        enabled = allSoftDrinks.isEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("Add Manually")
                    }
                }
            )
        }

        if (showUpdateDialog && selectedSoftDrinkToUpdate != null) {
            UpdateSoftDrinkDialog(
                softDrink = selectedSoftDrinkToUpdate!!,
                onUpdate = { updatedSoftDrink ->
                    softDrinkViewModel.updateSoftDrink(updatedSoftDrink)
                    successMessage = "${updatedSoftDrink.softDrinkName} updated successfully"
                    showSuccessMessage = true
                    showUpdateDialog = false
                },
                onDismiss = { showUpdateDialog = false }
            )
        }

        if (showDeleteConfirmation && softDrinkToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Delete Soft Drink") },
                text = { Text("Are you sure you want to delete this Soft drink?") },
                confirmButton = {
                    Button(
                        onClick = {
                            softDrinkViewModel.deleteSoftDrink(softDrinkToDelete!!)
                            showDeleteConfirmation = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("Confirm", color = Color.White)
                    }
                }
            )
        }

        if (showDeleteAllConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteAllConfirmation = false },
                title = { Text("Delete All Breads") },
                text = { Text("Are you sure you want to delete all bread items? This action cannot be undone.") },
                confirmButton = {
                    Button(
                        onClick = {
                            softDrinkViewModel.deleteAllSoftDrinks()
                            showDeleteAllConfirmation = false
                            successMessage = "All breads deleted successfully"
                            showSuccessMessage = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("Confirm", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteAllConfirmation = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun SoftDrinkItemCard(
    softDrink: SoftDrink,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = " ${softDrink.id} .",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " ${softDrink.softDrinkName} ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(" ${softDrink.softDrinkPrice}", style = MaterialTheme.typography.bodySmall)
                }
                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFD32F2F))
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateSoftDrinkDialog(
    softDrink: SoftDrink,
    onUpdate: (SoftDrink) -> Unit,
    onDismiss: () -> Unit
) {
    var softDrinkName by remember { mutableStateOf(softDrink.softDrinkName) }
    var softDrinkPrice by remember { mutableStateOf(softDrink.softDrinkPrice.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Soft Drink", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column {
                OutlinedTextField(
                    value = softDrinkName,
                    onValueChange = { softDrinkName = it },
                    label = { Text("Soft Drink Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = softDrinkPrice,
                    onValueChange = { softDrinkPrice = it },
                    label = { Text("Soft Drink Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = softDrinkPrice.toDoubleOrNull() ?: 0.0
                    val updatedSoftDrink = SoftDrink(
                        id = softDrink.id,
                        softDrinkName = softDrinkName,
                        softDrinkPrice = price
                    )
                    onUpdate(updatedSoftDrink)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Teal)
            ) {
                Text("Update")
            }
        }
    )
}
