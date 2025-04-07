package com.utopiatechhub.cashierpos.ui.connection

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress
import java.util.concurrent.Executors

// Sender: Scan Network
@Composable
fun SenderScreen() {
    var receiverIp by remember { mutableStateOf(listOf<String>()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            receiverIp = scanNetwork("192.168.0")
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Sender Screen", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(8.dp))
        Text("Found Receivers:")
        if (receiverIp.isEmpty()) {
            Text("No receivers found. Ensure they are on the same Wi-Fi.")
        } else {
            receiverIp.forEach { ip ->
                Button(onClick = { /* Handle something when a receiver button is clicked */ }) {
                    Text(ip)
                }
            }
        }
    }
}

fun scanNetwork(networkPrefix: String): List<String> {
    val foundIps = mutableListOf<String>()
    val executor = Executors.newFixedThreadPool(20)
    val tasks = mutableListOf<Runnable>()

    for (i in 1..254) {
        val ip = "$networkPrefix.$i"
        tasks.add(Runnable {
            try {
                val inetAddress = InetAddress.getByName(ip)
                if (inetAddress.isReachable(1500)) { // Increased timeout to 1.5s
                    synchronized(foundIps) {
                        foundIps.add(ip)
                        Log.d("NetworkScan", "Found receiver at: $ip")
                    }
                }
            } catch (_: Exception) {}
        })
    }

    tasks.forEach { executor.execute(it) }
    executor.shutdown()
    while (!executor.isTerminated) {
        // Thread.sleep(100)
    }
    return foundIps
}