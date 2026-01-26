package com.example.pmdm.archivebook.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pmdm.archivebook.ui.theme.ArchiveBookTheme
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // State for the input fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var keepSessionActive by remember { mutableStateOf(false) }

    // 1. Snackbar state and Coroutine Scope
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        // 2. Attach the SnackbarHost to the Scaffold
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Log in title
            Text(
                text = "Log In",
                style = MaterialTheme.typography.displayMedium,
                // This makes the text Brown in Light Mode and Cream in Dark Mode
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Section
            Text(
                text = "Email",
                modifier = Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("example@mail.com") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    // Makes the box background match the app background
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,

                    // Colors for the text you type
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,

                    //
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,

                    // Colors for the line underneath
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Section
            Text(
                text = "Password",
                modifier = Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Enter your password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button with Validation
            Button(
                onClick = {
                    // 3. Verify all fields are done
                    if (email.isBlank() || password.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please enter both email and password")
                        }
                    } else {
                        onLoginSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                // This makes the button look the same on both screens
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Button Background
                    contentColor = MaterialTheme.colorScheme.onPrimary  // Text Color
                )
            ) {
                Text(
                    text = "Log In",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Keep session active?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.width(8.dp))

                Switch(
                    checked = keepSessionActive,
                    onCheckedChange = { keepSessionActive = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,

                        uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        uncheckedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Sign Up redirection section
            Text(
                text = "Don't have an account yet?",
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onRegisterClick,
                modifier = Modifier.size(116.dp, 60.dp),
                shape = RoundedCornerShape(8.dp),
                // This makes the button look the same on both screens
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Button Background
                    contentColor = MaterialTheme.colorScheme.onPrimary  // Text Color
                )
            ) {
                Text(
                    text = "Sign In",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ArchiveBookTheme {
        LoginScreen(
            onLoginSuccess = {},
            onRegisterClick = {}
        )
    }
}