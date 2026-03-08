package org.russel.komandoandroid.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.russel.komandoandroid.data.auth.AuthState
import org.russel.komandoandroid.ui.viewmodel.AuthViewModel
import org.russel.komandoandroid.ui.viewmodel.GroupViewModel
import org.russel.komandoandroid.ui.viewmodel.TaskViewModel
import org.russel.komandoandroid.ui.viewmodel.UserViewModel

@Composable
fun RootNavGraph(
    navController: NavHostController,
    authState: AuthState,
    authViewModel: AuthViewModel,
    taskViewModel: TaskViewModel,
    groupViewModel: GroupViewModel,
    userViewModel: UserViewModel,
    topBarTitle: MutableState<String>,
    innerPadding: PaddingValues
) {

    val startDestination =
        if (authState == AuthState.LoggedIn) "main" else "auth"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        authNavGraph(
            navController,
            authViewModel
        )

        mainNavGraph(
            navController,
            taskViewModel,
            groupViewModel,
            userViewModel,
            authViewModel,
            topBarTitle,
            innerPadding
        )
    }
}