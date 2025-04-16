package com.utopiatechhub.cashierpos.ui.screen

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.utopiatechhub.cashierpos.authentication.UserRepository
import com.utopiatechhub.cashierpos.authentication.UserViewModel
import com.utopiatechhub.cashierpos.authentication.UserViewModelFactory
import com.utopiatechhub.cashierpos.database.AppDatabase
import com.utopiatechhub.cashierpos.language.LanguageDataStore
import com.utopiatechhub.cashierpos.ui.theme.Teal
import kotlinx.coroutines.launch

@Composable
fun ClientLoginScreen(navController: NavController) {
    var clientUserName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var clientCodeError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context.applicationContext)
    val tenantDataStore = TenantDataStore(context)
    val userRepository = UserRepository(database.userDao())
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(userRepository, tenantDataStore))
    val coroutineScope = rememberCoroutineScope()
    val languageDataStore = remember { LanguageDataStore(context) }
    var currentLanguage by remember { mutableStateOf("en") }

    // Observe language changes
    LaunchedEffect(Unit) {
        languageDataStore.languageFlow.collect { lang ->
            currentLanguage = lang
        }
    }

    // Language toggle function
    fun toggleLanguage() {
        coroutineScope.launch {
            val newLanguage = if (currentLanguage == "en") "am" else "en"
            languageDataStore.saveLanguage(newLanguage)
            Toast.makeText(context, "Language changed", Toast.LENGTH_SHORT).show()
            (context as? Activity)?.recreate()
        }
    }

    // Validate fields before login
    fun validateFields(): Boolean {
        return when {
            clientUserName.isEmpty() -> {
                errorMessage = context.getString(R.string.username_empty)
                clientCodeError = true
                false
            }
            password.isEmpty() -> {
                errorMessage = context.getString(R.string.password_empty)
                passwordError = true
                false
            }
            else -> true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(Color(0xFF80CBC4), Color.White)))
            .padding(16.dp)
    ) {
        // Language Toggle Icon (Top-Left)
        IconButton(
            onClick = { toggleLanguage() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            val icon = if (currentLanguage == "en") R.drawable.ic_english else R.drawable.ic_amharic
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Language Toggle",
                modifier = Modifier.size(28.dp),
                tint = Color.Unspecified // Don't tint flags
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.client_login),
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
                    clientCodeError = false
                },
                label = { Text(stringResource(R.string.client_code)) },
                isError = clientCodeError,
                modifier = Modifier.fillMaxWidth()
            )

            // Password Field
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = false
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
                isError = passwordError,
                modifier = Modifier.fillMaxWidth()
            )

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
                        errorMessage = null
                        userViewModel.loginUser(clientUserName, password) { user ->
                            isLoading = false
                            if (user != null && user.role == "client") {
                                navController.navigate("client")
                            } else {
                                errorMessage = context.getString(R.string.incorrect_credentials)
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
                    Text(text = stringResource(R.string.login), color = Color.White)
                }
            }

            // Navigation Links
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.admin_login),
                modifier = Modifier
                    .clickable { navController.navigate("adminLogin") }
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