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
import com.utopiatechhub.cashierpos.data.Juice
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.repository.JuiceRepository
import com.utopiatechhub.cashierpos.ui.theme.Teal
import com.utopiatechhub.cashierpos.viewmodel.JuiceViewModel
import com.utopiatechhub.cashierpos.viewmodel.factory.JuiceViewModelFactory
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageJuiceScreen(navController: NavController) {

    var showUpdateDialog by remember { mutableStateOf(false) }
    var selectedJuiceToUpdate by remember { mutableStateOf<Juice?>(null) }
    var showAddJuiceDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var juiceName by remember { mutableStateOf("") }
    var juicePrice by remember { mutableStateOf("") }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var juiceToDelete by remember { mutableStateOf<Juice?>(null) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context.applicationContext)
    val juiceRepository = JuiceRepository(database.juiceDao())
    val juiceViewModel: JuiceViewModel = viewModel(
        factory = JuiceViewModelFactory(juiceRepository)
    )
    val allJuices by juiceViewModel.allJuices.collectAsState(initial = emptyList())
    val filteredJuices = if (searchQuery.isEmpty()) allJuices else {
        allJuices.filter { it.juiceName.contains(searchQuery, ignoreCase = true) }
    }
    val isAddButtonEnabled = juiceName.isNotBlank() && juicePrice.isNotBlank()

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
                            label = { Text("Search Juice") },
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
                        Text("Manage Juices")
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
                    IconButton(onClick = { showAddJuiceDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Juice")
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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredJuices) { juice ->
                    JuiceItemCard(
                        juice = juice,
                        onEditClick = {
                            selectedJuiceToUpdate = juice
                            showUpdateDialog = true
                        },
                        onDeleteClick = {
                            juiceToDelete = juice
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

        if (showAddJuiceDialog) {
            AlertDialog(
                onDismissRequest = { showAddJuiceDialog = false },
                title = { Text("Add New Juice", style = MaterialTheme.typography.headlineSmall) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = juiceName,
                            onValueChange = { juiceName = it },
                            label = { Text("Juice Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = juicePrice,
                            onValueChange = { juicePrice = it },
                            label = { Text("Juice Price") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val price = juicePrice.toDoubleOrNull() ?: 0.0
                            val juice = Juice(juiceName = juiceName, juicePrice = price)
                            juiceViewModel.insertJuice(juice)
                            successMessage = "$juiceName added successfully"
                            showSuccessMessage = true
                            juiceName = ""
                            juicePrice = ""
                            showAddJuiceDialog = false
                        },
                        enabled = isAddButtonEnabled,
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("Add")
                    }
                    Button(
                        onClick = { juiceViewModel.addJuicesManually() },
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("Add Manually")
                    }
                }
            )
        }

        if (showUpdateDialog && selectedJuiceToUpdate != null) {
            UpdateJuiceDialog(
                juice = selectedJuiceToUpdate!!,
                onUpdate = { updatedJuice ->
                    juiceViewModel.updateJuice(updatedJuice)
                    successMessage = "${updatedJuice.juiceName} updated successfully"
                    showSuccessMessage = true
                    showUpdateDialog = false
                },
                onDismiss = { showUpdateDialog = false }
            )
        }

        if (showDeleteConfirmation && juiceToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Delete Juice") },
                text = { Text("Are you sure you want to delete this Juice?") },
                confirmButton = {
                    Button(
                        onClick = {
                            juiceViewModel.deleteJuice(juiceToDelete!!)
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
fun JuiceItemCard(
    juice: Juice,
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
                    text = " ${juice.id} .",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " ${juice.juiceName} ",
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
                    Text(" ${juice.juicePrice}", style = MaterialTheme.typography.bodySmall)
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
fun UpdateJuiceDialog(
    juice: Juice,
    onUpdate: (Juice) -> Unit,
    onDismiss: () -> Unit
) {
    var juiceName by remember { mutableStateOf(juice.juiceName) }
    var juicePrice by remember { mutableStateOf(juice.juicePrice.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Juice", style = MaterialTheme.typography.headlineSmall) },
        text = {
            Column {
                OutlinedTextField(
                    value = juiceName,
                    onValueChange = { juiceName = it },
                    label = { Text("Juice Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = juicePrice,
                    onValueChange = { juicePrice = it },
                    label = { Text("Juice Price") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val price = juicePrice.toDoubleOrNull() ?: 0.0
                    val updatedJuice = Juice(
                        id = juice.id,
                        juiceName = juiceName,
                        juicePrice = price
                    )
                    onUpdate(updatedJuice)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Teal)
            ) {
                Text("Update")
            }
        }
    )
}
