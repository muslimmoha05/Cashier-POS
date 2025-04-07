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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.utopiatechhub.cashierpos.authentication.TenantDataStore
import com.utopiatechhub.cashierpos.authentication.UserRepository
import com.utopiatechhub.cashierpos.authentication.UserViewModel
import com.utopiatechhub.cashierpos.authentication.UserViewModelFactory
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.ui.theme.Teal

@Composable
fun ClientLoginScreen(navController: NavController) {
    var clientUserName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var clientCodeError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } // Loading state
    var errorMessage by remember { mutableStateOf<String?>(null) } // Generic error message

    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context.applicationContext)
    val tenantDataStore = TenantDataStore(context)
    val userRepository = UserRepository(database.userDao())
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(userRepository, tenantDataStore)
    )

    // Validate fields before login
    fun validateFields(): Boolean {
        return when {
            clientUserName.isEmpty() -> {
                errorMessage = "Username cannot be empty"
                clientCodeError = true
                false
            }
            password.isEmpty() -> {
                errorMessage = "Password cannot be empty"
                passwordError = true
                false
            }
            else -> true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF80CBC4), Color.White)
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Client Login",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Teal
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Client Code Field
            OutlinedTextField(
                value = clientUserName,
                onValueChange = {
                    clientUserName = it
                    clientCodeError = false // Reset error on change
                },
                label = { Text("Client Code") },
                isError = clientCodeError,
                modifier = Modifier.fillMaxWidth()
            )

            // Password Field
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = false // Reset error on change
                },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Password Visibility"
                        )
                    }
                },
                isError = passwordError,
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

            // Login Button
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (validateFields()) {
                        isLoading = true
                        errorMessage = null // Reset error message
                        userViewModel.loginUser(clientUserName, password) { user ->
                            isLoading = false
                            if (user != null && user.role == "client") {
                                navController.navigate("client")
                            } else {
                                errorMessage = "Incorrect client code or password"
                                clientCodeError = true
                                passwordError = true
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
                    Text(text = "Login", color = Color.White)
                }
            }

            // Navigation Links
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Admin LOGIN",
                modifier = Modifier
                    .clickable { navController.navigate("adminLogin") }
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                color = Teal
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Don't have an account? REGISTER",
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