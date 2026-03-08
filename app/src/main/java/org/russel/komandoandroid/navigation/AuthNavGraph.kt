package org.russel.komandoandroid.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.russel.komandoandroid.ui.viewmodel.AuthViewModel
import org.russel.komandoandroid.ui.login.LoginScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    navigation(
        startDestination = "login",
        route = "auth"
    ) {

        composable("login") {

            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    authViewModel.loginSuccess()
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }
    }
}