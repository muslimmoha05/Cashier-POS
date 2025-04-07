package com.utopiatechhub.cashierpos.ui.screen.admin.managemenu

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.utopiatechhub.cashierpos.data.FoodCategory
import com.utopiatechhub.cashierpos.ui.theme.Teal
import com.utopiatechhub.cashierpos.viewmodel.FoodCategoryViewModel
import java.io.File
import java.io.FileOutputStream
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageFoodCategoryScreen(
    navController: NavController,
    foodCategoryViewModel: FoodCategoryViewModel,
    context: Context
) {
    val categories by foodCategoryViewModel.categories.collectAsStateWithLifecycle()

    var categoryName by remember { mutableStateOf("") }
    var translatedName by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<FoodCategory?>(null) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Image Picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            photoUri = uri
        } else {
            showError = true
            errorMessage = "Image selection was canceled or failed"
        }
    }

    // Filter categories by search query
    val filteredCategories = if (searchQuery.isEmpty()) categories else {
        categories.filter { it.categoryName.contains(searchQuery, ignoreCase = true) }
    }
    val isAddButtonEnabled = categoryName.isNotBlank() && translatedName.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Search Category") },
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
                        Text("Manage Food Categories")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Category")
                    }
                    Button(
                        onClick = { foodCategoryViewModel.addCategoryManually() },
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("Add Manually")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn {
                items(filteredCategories, key = { it.id }) { category ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
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
                                    text = " ${category.id} .",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = " ${category.categoryName} ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(" ${category.translatedName}", color = Color.Gray, fontSize = 18.sp, style = MaterialTheme.typography.bodySmall)
                                    // Load and display the image using Coil
                                    if (category.photoUrl.isNotEmpty()) {
                                        val file = File(category.photoUrl)
                                        if (file.exists()) {
                                            Image(
                                                painter = rememberAsyncImagePainter(file),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(100.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Text("Image not found", color = Color.Red)
                                        }
                                    } else {
                                        Text("No image available", color = Color.Gray)
                                    }
                                }
                                Row {
                                    IconButton(
                                        onClick = {
                                            selectedCategory = category
                                            categoryName = category.categoryName
                                            translatedName = category.translatedName
                                            photoUri = category.photoUrl.toUri()
                                            showUpdateDialog = true
                                        }
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(
                                        onClick = {
                                            selectedCategory = category
                                            showDeleteDialog = true
                                        }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFD32F2F))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add Category Dialog
        if (showAddDialog) {
            CategoryDialog(
                title = "Add Food Category",
                categoryName = categoryName,
                translatedName = translatedName,
                photoUrl = photoUri?.toString() ?: "",
                onCategoryNameChange = { categoryName = it },
                onTranslatedNameChange = { translatedName = it },
                onConfirm = {
                    val filePath = photoUri?.let { uri ->
                        saveImageToInternalStorage(context, uri)
                    }
                    val newCategory = FoodCategory(
                        categoryName = categoryName,
                        translatedName = translatedName,
                        photoUrl = filePath ?: ""
                    )
                    foodCategoryViewModel.addCategory(newCategory)

                    // Reset fields after adding
                    categoryName = ""
                    translatedName = ""
                    photoUri = null
                    showAddDialog = false
                },
                enabled = isAddButtonEnabled,
                onDismiss = { showAddDialog = false },
                onPickImage = {
                    imagePickerLauncher.launch("image/*")
                }
            )
        }

        // Update Category Dialog
        if (showUpdateDialog) {
            CategoryDialog(
                title = "Update Food Category",
                categoryName = categoryName,
                translatedName = translatedName,
                photoUrl = photoUri?.toString() ?: "",
                onCategoryNameChange = { categoryName = it },
                onTranslatedNameChange = { translatedName = it },
                onConfirm = {
                    val filePath = photoUri?.let { uri ->
                        saveImageToInternalStorage(context, uri)
                    }
                    selectedCategory?.let { category ->
                        val updatedCategory = category.copy(
                            categoryName = categoryName,
                            translatedName = translatedName,
                            photoUrl = filePath ?: category.photoUrl
                        )
                        foodCategoryViewModel.updateFoodCategory(updatedCategory)
                    }
                    showUpdateDialog = false
                },
                onDismiss = { showUpdateDialog = false },
                onPickImage = {
                    imagePickerLauncher.launch("image/*")
                },
                enabled = isAddButtonEnabled
            )
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Category") },
                text = { Text("Are you sure you want to delete this category?") },
                confirmButton = {
                    Button(
                        onClick = {
                        selectedCategory?.let { foodCategoryViewModel.deleteFoodCategory(it) }
                        showDeleteDialog = false
                    }, colors = ButtonDefaults.buttonColors(containerColor = Teal)) {
                        Text("Delete")
                    }
                }
            )
        }

        // Error Dialog
        if (showError) {
            AlertDialog(
                onDismissRequest = { showError = false },
                title = { Text("Error") },
                text = { Text(errorMessage) },
                confirmButton = {
                    Button(
                        onClick = { showError = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

fun saveImageToInternalStorage(context: Context, imageUri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val file = File(context.filesDir, "image_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        file.absolutePath // Return the file path
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun CategoryDialog(
    title: String,
    categoryName: String,
    translatedName: String,
    photoUrl: String,
    onCategoryNameChange: (String) -> Unit,
    onTranslatedNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onPickImage: () -> Unit,
    enabled: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = onCategoryNameChange,
                    label = { Text("Category Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = translatedName,
                    onValueChange = onTranslatedNameChange,
                    label = { Text("Translated Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = photoUrl,
                    onValueChange = { /* No longer needed */},
                    label = { Text("Photo URL") },
                    trailingIcon = {
                        IconButton(onClick = onPickImage) {
                            Icon(Icons.Default.Add, contentDescription = "Pick Image")
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = enabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Teal,
                    contentColor = Color.White
                )
            ) {
                Text("Save")
            }
        }
    )
}