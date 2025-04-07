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
import com.utopiatechhub.cashierpos.data.Cake
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.repository.CakeRepository
import com.utopiatechhub.cashierpos.ui.theme.Teal
import com.utopiatechhub.cashierpos.viewmodel.CakeViewModel
import com.utopiatechhub.cashierpos.viewmodel.factory.CakeViewModelFactory
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCakeScreen(navController: NavController) {

    val context = LocalContext.current
    val cakeViewModel: CakeViewModel = viewModel(factory = CakeViewModelFactory(CakeRepository(AppDatabase.getDatabase(context).cakeDao())))
    val allCakes by cakeViewModel.allCakes.collectAsState()

    var cakeToDelete by remember { mutableStateOf<Cake?>(null) }
    var selectedCakeToUpdate by remember { mutableStateOf<Cake?>(null) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showAddCakeDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var cakeName by remember { mutableStateOf("") }
    var cakePrice by remember { mutableStateOf("") }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    val filteredCakes = if (searchQuery.isEmpty()) allCakes else {
        allCakes.filter { it.cakeName.contains(searchQuery, ignoreCase = true) }
    }
    val isAddButtonEnabled = cakeName.isNotBlank() && cakePrice.isNotBlank()

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
                            label = { Text("Search Cake") },
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
                        Text("Manage Cakes")
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
                    IconButton(onClick = { showAddCakeDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Cake")
                    }
                    IconButton(onClick = {
                        isSearchActive = !isSearchActive
                        if (!isSearchActive) searchQuery = ""
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(filteredCakes, key = { it.id }) { cake ->
                    CakeItemCard(
                        cake = cake,
                        onEditClick = {
                            selectedCakeToUpdate = cake
                            showUpdateDialog = true
                        },
                        onDeleteClick = {
                            cakeToDelete = cake
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

        // Add Cake Dialog
        if (showAddCakeDialog) {
            AlertDialog(
                onDismissRequest = { showAddCakeDialog = false },
                title = { Text("Add New Cake", style = MaterialTheme.typography.headlineSmall) },
                text = {
                    Column {
                        // Cake Name Input
                        OutlinedTextField(
                            value = cakeName,
                            onValueChange = { cakeName = it },
                            label = { Text("Cake Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Cake Price Input
                        OutlinedTextField(
                            value = cakePrice,
                            onValueChange = { cakePrice = it },
                            label = { Text("Cake Price") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val price = cakePrice.toDoubleOrNull() ?: 0.0
                            val cake = Cake(cakeName = cakeName, cakePrice = price)
                            cakeViewModel.insertCake(cake)
                            // Show success message
                            successMessage = "$cakeName added successfully"
                            showSuccessMessage = true
                            // Reset fields after adding
                            cakeName = ""
                            cakePrice = ""
                            showAddCakeDialog = false
                        },
                        enabled = isAddButtonEnabled,
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("Add")
                    }
                    Button(
                        onClick = { cakeViewModel.addCakeManually() },
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("Add Manually")
                    }
                }
            )
        }

        // Update Cake Dialog
        if (showUpdateDialog && selectedCakeToUpdate != null) {
            UpdateCakeDialog(
                cake = selectedCakeToUpdate!!,
                onUpdate = { updatedCake ->
                    cakeViewModel.updateCake(updatedCake)
                    // Show success message
                    successMessage = "${updatedCake.cakeName} updated successfully"
                    showSuccessMessage = true
                    showUpdateDialog = false
                },
                onDismiss = { showUpdateDialog = false }
            )
        }

        // Delete Confirmation Dialog
        if (showDeleteConfirmation && cakeToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Delete Cake") },
                text = { Text("Are you sure you want to delete this cake?") },
                confirmButton = {
                    Button(
                        onClick = {
                            cakeViewModel.deleteCake(cakeToDelete!!)
                            showDeleteConfirmation = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("Confirm", color = Color.White)
                    }
                }
            )
        }
    }
}

@Composable
fun CakeItemCard(
    cake: Cake,
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
                    text = " ${cake.id} .",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " ${cake.cakeName} ",
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
                    Text(" ${cake.cakePrice}", style = MaterialTheme.typography.bodySmall)
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
fun UpdateCakeDialog(
    cake: Cake,
    onUpdate: (Cake) -> Unit,
    onDismiss: () -> Unit
) {
    var cakeName by remember { mutableStateOf(cake.cakeName) }
    var cakePrice by remember { mutableStateOf(cake.cakePrice.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Cake", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column {
                // Cake Name Input
                OutlinedTextField(
                    value = cakeName,
                    onValueChange = { cakeName = it },
                    label = { Text("Cake Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Cake Price Input
                OutlinedTextField(
                    value = cakePrice,
                    onValueChange = { cakePrice = it },
                    label = { Text("Cake Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = cakePrice.toDoubleOrNull() ?: 0.0
                    val updatedCake = Cake(
                        id = cake.id,
                        cakeName = cakeName,
                        cakePrice = price
                    )
                    onUpdate(updatedCake)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Teal)
            ) {
                Text("Update")
            }
        }
    )
}