package com.utopiatechhub.cashierpos.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.utopiatechhub.cashierpos.language.LanguageDataStore
import com.utopiatechhub.cashierpos.language.LocaleHelper
import com.utopiatechhub.cashierpos.ui.screen.AdminLoginScreen
import com.utopiatechhub.cashierpos.ui.screen.ClientLoginScreen
import com.utopiatechhub.cashierpos.ui.screen.RegisterScreen
import com.utopiatechhub.cashierpos.ui.theme.CashierPOSTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@RequiresApi(Build.VERSION_CODES.Q)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CashierPOSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val lang = runBlocking {
            LanguageDataStore(newBase).languageFlow.first()
        }
        super.attachBaseContext(LocaleHelper.wrap(newBase, lang))
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    RequestNotificationPermission()
    RequestNearbyDevicesPermission()

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content with navigation
        Scaffold(modifier = Modifier.matchParentSize()) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "clientLogin",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("clientLogin") { ClientLoginScreen(navController) }
                composable("adminLogin") { AdminLoginScreen(navController) }
                composable("register") { RegisterScreen(navController) }
                composable("admin") { AdminScreen() }
                composable("client") {
                    ClientScreen()
                }
            }
        }
    }
}

@Composable
fun RequestNearbyDevicesPermission() {
    val context = LocalContext.current

    // Check if the device is running Android 13 (API level 33) or higher
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (!isGranted) {
                // Handle rejection (optional)
                Toast.makeText(context, "Nearby Devices permission is required for Bluetooth and Wi-Fi features", Toast.LENGTH_SHORT).show()
            }
        }

        val hasPermission = remember {
            ContextCompat.checkSelfPermission(context, Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_GRANTED
        }

        LaunchedEffect(hasPermission) {
            if (!hasPermission) {
                permissionLauncher.launch(Manifest.permission.NEARBY_WIFI_DEVICES)
            }
        }
    } else {
        // For devices below Android 13, use alternative permissions
        RequestLegacyBluetoothPermissions()
    }
}

@Composable
fun RequestLegacyBluetoothPermissions() {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.BLUETOOTH] != true ||
            permissions[Manifest.permission.BLUETOOTH_ADMIN] != true ||
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] != true
        ) {
            // Handle rejection (optional)
            Toast.makeText(context, "Bluetooth and Location permissions are required", Toast.LENGTH_SHORT).show()
        }
    }

    val requiredPermissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val hasPermissions = remember {
        requiredPermissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    LaunchedEffect(hasPermissions) {
        if (!hasPermissions) {
            permissionLauncher.launch(requiredPermissions)
        }
    }
}

@Composable
fun RequestNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val context = LocalContext.current
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (!isGranted) {
                // Handle rejection (optional)
                Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        val hasPermission = remember {
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        }

        LaunchedEffect(hasPermission) {
            if (!hasPermission) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}






