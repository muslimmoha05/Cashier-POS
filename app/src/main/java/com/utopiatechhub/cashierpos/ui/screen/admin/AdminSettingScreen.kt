package com.utopiatechhub.cashierpos.ui.screen.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.utopiatechhub.cashierpos.synchronization.SyncRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AdminSettingScreen(repository: SyncRepository) {
    var syncStatus by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                isLoading = true
                syncStatus = "" // Reset status before sync
                coroutineScope.launch {
                    syncData(repository) { result ->
                        isLoading = false
                        syncStatus = result
                    }
                }
            },
            enabled = !isLoading
        ) {
            Text(text = if (isLoading) "Syncing..." else "Sync Data")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = syncStatus,
            modifier = Modifier.fillMaxWidth(),
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}

private fun syncData(repository: SyncRepository, onResult: (String) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            repository.syncData()
            withContext(Dispatchers.Main) {
                onResult("Sync Successful")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onResult("Sync Failed: ${e.message}")
            }
        }
    }
}