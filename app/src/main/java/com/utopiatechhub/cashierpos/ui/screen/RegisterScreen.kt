package com.utopiatechhub.cashierpos.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController) {
    var businessName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var role by remember { mutableStateOf<String?>(null) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isUsernameValid by remember { mutableStateOf<Boolean?>(null) } // To track username validation result

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context.applicationContext)
    val tenantDataStore = TenantDataStore(context)
    val userRepository = UserRepository(database.userDao())
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(userRepository, tenantDataStore)
    )

    // Validate fields synchronously
    fun validateFields(): Boolean {
        if (businessName.isEmpty()) {
            validationError = context.getString(R.string.business_name_required)
            return false
        }
        if (username.isEmpty()) {
            validationError = context.getString(R.string.username_required)
            return false
        }
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            validationError = context.getString(R.string.password_required)
            return false
        }

        if (password != confirmPassword) {
            validationError = context.getString(R.string.passwords_do_not_match)
            return false
        }
        if (role == null) {
            validationError = context.getString(R.string.role_required)
            return false
        }
        return true
    }

    LaunchedEffect(role) {
        if (username.isNotEmpty()) {
            userViewModel.isUsernameValid(username, role!!) { isValid, errorMsg ->
                validationError = errorMsg
                isUsernameValid = isValid
            }
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
                text = stringResource(R.string.register_button_text),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 32.sp, color = Teal),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = businessName,
                onValueChange = { businessName = it },
                label = { Text(stringResource(R.string.business_name)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(stringResource(R.string.username)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
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
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(stringResource(R.string.confirm_password)) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = role == "client",
                    onClick = { role = "client" },
                    colors = RadioButtonDefaults.colors(selectedColor = Teal, unselectedColor = Color.Gray)
                )
                Text(stringResource(R.string.client))
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = role == "admin",
                    onClick = { role = "admin" },
                    colors = RadioButtonDefaults.colors(selectedColor = Teal, unselectedColor = Color.Gray)
                )
                Text(stringResource(R.string.admin))
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (validationError != null) {
                Text(
                    text = validationError!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (validateFields() && isUsernameValid == true) {
                        val tenantId = System.currentTimeMillis()
                        userViewModel.registerUser(businessName, username, password, role!!, tenantId) { success ->
                            if (success) {
                                successMessage = context.getString(R.string.registration_successful)
                                coroutineScope.launch {
                                    delay(3000)
                                    successMessage = null
                                    navController.navigate(if (role == "client") "clientLogin" else "adminLogin")
                                }
                            } else {
                                errorMessage = context.getString(R.string.registration_failed)
                                coroutineScope.launch {
                                    delay(3000)
                                    errorMessage = null
                                }
                            }
                        }
                    } else if (isUsernameValid == false) {
                        validationError = context.getString(R.string.username_taken)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Teal)
            ) {
                Text(text = stringResource(R.string.register_button_text), color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.already_have_account),
                modifier = Modifier
                    .clickable { navController.navigate("clientLogin") }
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                color = Teal
            )

            successMessage?.let {
                Text(
                    text = it,
                    color = Teal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

