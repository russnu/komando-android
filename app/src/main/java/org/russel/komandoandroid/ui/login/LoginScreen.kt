package org.russel.komandoandroid.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.russel.komandoandroid.ui.component.AppPasswordField
import org.russel.komandoandroid.ui.component.AppPrimaryButton
import org.russel.komandoandroid.ui.component.AppTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onLoginSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Login") })
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            AppTextField(
                value = username,
                onValueChange = { username = it },
                label = "Email"
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppPasswordField(
                value = password,
                onValueChange = { password = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            AppPrimaryButton(
                text = if (uiState.isLoading) "Logging in..." else "Login",
                onClick = {
                    viewModel.login(username, password)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onRegisterClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Don't have an account? Register")
            }
        }
    }
}