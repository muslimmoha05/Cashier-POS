package com.utopiatechhub.cashierpos.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.utopiatechhub.cashierpos.R
import com.utopiatechhub.cashierpos.authentication.TenantDataStore
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.authentication.UserRepository
import com.utopiatechhub.cashierpos.ui.theme.Teal
import com.utopiatechhub.cashierpos.authentication.UserViewModel
import com.utopiatechhub.cashierpos.authentication.UserViewModelFactory

@Composable
fun AdminLoginScreen(navController: NavController) {
    var adminUserName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var adminUsernameError by remember { mutableStateOf(false) }
    var adminPasswordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context.applicationContext)
    val tenantDataStore = TenantDataStore(context)
    val userRepository = UserRepository(database.userDao())
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(userRepository, tenantDataStore))

    // Validate fields before login
    fun validateFields(): Boolean {
        return when {
            adminUserName.isEmpty() -> {
                errorMessage = context.getString(R.string.username_empty)
                adminUsernameError = true
                false
            }
            password.isEmpty() -> {
                errorMessage = context.getString(R.string.password_empty)
                adminPasswordError = true
                false
            }
            else -> true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(Color(0xFF80CBC4), Color.White)))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.admin_login),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp, color = Teal),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Username Field
            OutlinedTextField(
                value = adminUserName,
                onValueChange = {
                    adminUserName = it
                    adminUsernameError = false
                },
                label = { Text(stringResource(R.string.admin_code)) },
                isError = adminUsernameError,
                modifier = Modifier.fillMaxWidth()
            )

            // Password Field
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    adminPasswordError = false
                },
                label = { Text(stringResource(R.string.password)) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Password Visibility"
                        )
                    }
                },
                isError = adminPasswordError,
                modifier = Modifier.fillMaxWidth()
            )

            // Error Message
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (validateFields()) {
                        isLoading = true
                        errorMessage = null
                        userViewModel.loginUser(adminUserName, password) { user ->
                            isLoading = false
                            if (user != null && user.role == "admin") {
                                navController.navigate("admin")
                            } else {
                                errorMessage = context.getString(R.string.incorrect_credentials)
                                adminUsernameError = true
                                adminPasswordError = true
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Teal)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(text = stringResource(R.string.login), color = Color.White)
                }
            }

            // Navigation Links
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.client_login),
                modifier = Modifier
                    .clickable { navController.navigate("clientLogin") }
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                color = Teal
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.register),
                modifier = Modifier
                    .clickable { navController.navigate("register") }
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                color = Teal
            )
        }
    }
}