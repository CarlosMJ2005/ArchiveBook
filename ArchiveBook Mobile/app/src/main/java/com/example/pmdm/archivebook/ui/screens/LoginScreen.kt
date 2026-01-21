package com.example.pmdm.archivebook.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pmdm.archivebook.ui.theme.ArchiveBookTheme

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    // State for the input fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp), // Extra side padding for a better look
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Log in text
            Text(
                text = "Log in",
                style = MaterialTheme.typography.displayMedium
            )
            // Email Section
            Text(text = "Email",
                modifier = Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.titleLarge
            )
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("example@mail.com") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Password Section
            Text(text = "Password",
                modifier = Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.titleLarge
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Enter your password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(), // Hides characters
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(32.dp))
            // Login Button
            Button(
                onClick = onLoginSuccess,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Login",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.height(48.dp))
            //  Sign in text
            Text(
                text = "Don't have an account yet?",
                style = MaterialTheme.typography.titleLarge,
            )
            //  Sign in Buttom
            Button(
                onClick = {},
                modifier = Modifier.padding(8.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text("Sign in",
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
        LoginScreen(onLoginSuccess = {})
    }
}