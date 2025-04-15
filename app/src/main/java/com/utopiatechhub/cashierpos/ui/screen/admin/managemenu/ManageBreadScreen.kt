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
import androidx.compose.material.icons.filled.DeleteSweep
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
import com.utopiatechhub.cashierpos.data.Bread
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.repository.BreadRepository
import com.utopiatechhub.cashierpos.ui.theme.Teal
import com.utopiatechhub.cashierpos.viewmodel.BreadViewModel
import com.utopiatechhub.cashierpos.viewmodel.factory.BreadViewModelFactory
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageBreadScreen(navController: NavController) {

    var showUpdateDialog by remember { mutableStateOf(false) }
    var selectedBreadToUpdate by remember { mutableStateOf<Bread?>(null) }
    var showAddBreadDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var breadName by remember { mutableStateOf("") }
    var breadPrice by remember { mutableStateOf("") }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var breadToDelete by remember { mutableStateOf<Bread?>(null) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var showDeleteAllConfirmation by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context.applicationContext)
    val breadRepository = BreadRepository(database.breadDao())
    val breadViewModel: BreadViewModel = viewModel(factory = BreadViewModelFactory(breadRepository))
    val allBreads by breadViewModel.allBreads.collectAsState(initial = emptyList())
    val filteredBreads = if (searchQuery.isEmpty()) allBreads else { allBreads.filter { it.breadName.contains(searchQuery, ignoreCase = true) } }
    val isAddButtonEnabled = breadName.isNotBlank() && breadPrice.isNotBlank()

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
                            label = { Text("Search Bread") },
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
                        Text("Manage Breads")
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
                    IconButton(onClick = { showAddBreadDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Bread")
                    }
                    IconButton(onClick = {
                        isSearchActive = !isSearchActive
                        if (!isSearchActive) searchQuery = ""
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    if (allBreads.isNotEmpty()) {
                        IconButton(onClick = { showDeleteAllConfirmation = true }) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Delete All Breads")
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
                items(filteredBreads) { bread ->
                    BreadItemCard(
                        bread = bread,
                        onEditClick = {
                            selectedBreadToUpdate = bread
                            showUpdateDialog = true
                        },
                        onDeleteClick = {
                            breadToDelete = bread
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

        if (showAddBreadDialog) {
            AlertDialog(
                onDismissRequest = { showAddBreadDialog = false },
                title = { Text("Add New Bread", style = MaterialTheme.typography.headlineSmall) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = breadName,
                            onValueChange = { breadName = it },
                            label = { Text("Bread Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = breadPrice,
                            onValueChange = { breadPrice = it },
                            label = { Text("Bread Price") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val price = breadPrice.toDoubleOrNull() ?: 0.0
                            val bread = Bread(breadName = breadName, breadPrice = price)
                            breadViewModel.insertBread(bread)
                            successMessage = "$breadName added successfully"
                            showSuccessMessage = true
                            breadName = ""
                            breadPrice = ""
                            showAddBreadDialog = false
                        },
                        enabled = isAddButtonEnabled,
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("Add")
                    }
                    Button(
                        onClick = { breadViewModel.addBreadsManually() },
                        enabled = allBreads.isEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("Add Manually")
                    }
                }
            )
        }

        if (showUpdateDialog && selectedBreadToUpdate != null) {
            UpdateBreadDialog(
                bread = selectedBreadToUpdate!!,
                onUpdate = { updatedBread ->
                    breadViewModel.updateBread(updatedBread)
                    successMessage = "${updatedBread.breadName} updated successfully"
                    showSuccessMessage = true
                    showUpdateDialog = false
                },
                onDismiss = { showUpdateDialog = false }
            )
        }

        if (showDeleteConfirmation && breadToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Delete Bread") },
                text = { Text("Are you sure you want to delete this Bread?") },
                confirmButton = {
                    Button(
                        onClick = {
                            breadViewModel.deleteBread(breadToDelete!!)
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
                            breadViewModel.deleteAllBreads()
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
fun BreadItemCard(
    bread: Bread,
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
                    text = " ${bread.id} .",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " ${bread.breadName} ",
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
                        Text(" ${bread.breadPrice}", style = MaterialTheme.typography.bodySmall)
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
fun UpdateBreadDialog(
    bread: Bread,
    onUpdate: (Bread) -> Unit,
    onDismiss: () -> Unit
) {
    var breadName by remember { mutableStateOf(bread.breadName) }
    var breadPrice by remember { mutableStateOf(bread.breadPrice.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Bread", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column {
                OutlinedTextField(
                    value = breadName,
                    onValueChange = { breadName = it },
                    label = { Text("Bread Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = breadPrice,
                    onValueChange = { breadPrice = it },
                    label = { Text("Bread Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = breadPrice.toDoubleOrNull() ?: 0.0
                    val updatedBread = Bread(
                        id = bread.id,
                        breadName = breadName,
                        breadPrice = price
                    )
                    onUpdate(updatedBread)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Teal)
            ) {
                Text("Update")
            }
        }
    )
}
