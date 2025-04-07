package com.utopiatechhub.cashierpos.ui.screen.client.bottomandsidenav

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.utopiatechhub.cashierpos.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDashboardScreen() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Client Dashboard", fontWeight = FontWeight.Bold) },
                actions = {
                    // action
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Placeholder Image
            Image(
                painter = painterResource(id = R.drawable.baseline_notifications_active_24), // Replace with your image resource
                contentDescription = "Placeholder Image",
                modifier = Modifier
                    .size(150.dp) // Set the size of the image
                    .clip(RoundedCornerShape(8.dp)), // Optional: add rounded corners
                contentScale = ContentScale.Crop // Optional: scale the image
            )

            Spacer(modifier = Modifier.height(16.dp)) // Add some space between the image and text

            // Placeholder Text
            Text(
                text = "Welcome to the Admin Dashboard!",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp)) // Add some space below the text

            // Additional placeholder text or content can be added here
            Text(
                text = "Here you can manage your clients and view analytics.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
