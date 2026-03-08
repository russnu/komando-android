package org.russel.komandoandroid

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import org.russel.komandoandroid.data.auth.AuthState
import org.russel.komandoandroid.ui.viewmodel.AuthViewModel
import org.russel.komandoandroid.data.auth.SessionManager
import org.russel.komandoandroid.data.remote.RetrofitClient
import org.russel.komandoandroid.data.repository.AuthRepository
import org.russel.komandoandroid.data.repository.DeviceRepository
import org.russel.komandoandroid.data.repository.GroupRepository
import org.russel.komandoandroid.data.repository.TaskRepository
import org.russel.komandoandroid.data.repository.UserRepository
import org.russel.komandoandroid.fcmservice.FcmTopicManager
import org.russel.komandoandroid.navigation.RootNavGraph
import org.russel.komandoandroid.ui.component.AppTopBar
import org.russel.komandoandroid.data.model.NavItem
import org.russel.komandoandroid.ui.factory.AuthViewModelFactory
import org.russel.komandoandroid.ui.factory.GroupViewModelFactory
import org.russel.komandoandroid.ui.factory.TaskViewModelFactory
import org.russel.komandoandroid.ui.factory.UserViewModelFactory
import org.russel.komandoandroid.ui.viewmodel.GroupViewModel
import org.russel.komandoandroid.ui.viewmodel.TaskViewModel
import org.russel.komandoandroid.ui.theme.KomandoandroidTheme
import org.russel.komandoandroid.ui.viewmodel.UserViewModel


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }
        createNotificationChannel();
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM", "Token: $token")
        }

        enableEdgeToEdge()
        val sessionManager = SessionManager(application)

        if (sessionManager.isLoggedIn()) {
            FcmTopicManager.restoreSubscriptions(sessionManager)
        }
        setContent {
            KomandoandroidTheme(darkTheme = false, dynamicColor = false) {
                // ------------------------------------------------------------ //
                val authApi  = RetrofitClient.authService(applicationContext)
                val deviceApi = RetrofitClient.deviceService(applicationContext)
                val taskApi  = RetrofitClient.taskService(applicationContext)
                val groupApi  = RetrofitClient.groupService(applicationContext)
                val userApi  = RetrofitClient.userService(applicationContext)
                // ------------------------------------------------------------ //
                val deviceRepository = DeviceRepository(deviceApi)
                val authRepository = AuthRepository(
                    api = authApi,
                    sessionManager = sessionManager,
                )
                val taskRepository = TaskRepository(taskApi)
                val groupRepository = GroupRepository(groupApi)
                val userRepository = UserRepository(userApi)
                // ------------------------------------------------------------ //

                val taskViewModel: TaskViewModel = viewModel(
                    factory = TaskViewModelFactory(taskRepository, userRepository, sessionManager)
                )
//                val profileViewModel: ProfileViewModel = viewModel(
//                    factory = ProfileViewModelFactory(sessionManager)
//                )
                val groupViewModel: GroupViewModel = viewModel(
                    factory = GroupViewModelFactory(groupRepository, sessionManager)
                )
//                val loginViewModel: LoginViewModel = viewModel(
//                    factory = LoginViewModelFactory(authRepository, deviceRepository, groupViewModel , sessionManager)
//                )
                val userViewModel: UserViewModel = viewModel(
                    factory = UserViewModelFactory(userRepository, sessionManager)
                )
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(authRepository, deviceRepository, groupViewModel,sessionManager)
                )


                // ======================================================================== //
                val authState by authViewModel.authState.collectAsState()
                val navController = rememberNavController()
                LaunchedEffect(authState) {
                    when (authState) {
                        AuthState.LoggedOut -> {
                            navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }

                        AuthState.LoggedIn -> {
                            navController.navigate(NavItem.Tasks.route) {
                                popUpTo("login") { inclusive = true }
                                launchSingleTop = true
                            }
                        }

                        else -> {}
                    }
                }
                val items = listOf(
                    NavItem.Tasks,
                    NavItem.Groups,
                    NavItem.Profile
                )

                val topBarTitle = remember { mutableStateOf("") }
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

                Scaffold(
                    modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        val canGoBack = navController.previousBackStackEntry != null
                        AppTopBar(
                            title = topBarTitle.value,
                            showBackButton = canGoBack,
                            scrollBehavior = scrollBehavior,
                            onBackClick = { navController.popBackStack() }
                        )
                    },
                    bottomBar = {
                        val currentRoute =
                            navController.currentBackStackEntryAsState().value?.destination?.route

                        val bottomBarRoutes = setOf(
                            NavItem.Tasks.route,
                            NavItem.Groups.route,
                            NavItem.Profile.route
                        )

                        val showBottomBar = bottomBarRoutes.any { currentRoute?.startsWith(it) == true }

                        if (showBottomBar) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                items.forEach { item ->
                                    NavigationBarItem(
                                        selected = currentRoute == item.route,
                                        onClick = {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.startDestinationId)
                                                launchSingleTop = true
                                            }
                                        },
                                        icon = { Icon(painter = painterResource(id = item.icon), contentDescription = item.label) },
                                        label = { Text(item.label) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                                            unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                            unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                            indicatorColor = MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                }
                            }
                        }

                    }) { innerPadding ->

                    RootNavGraph(
                        navController = navController,
                        authState = authState,
                        authViewModel = authViewModel,
//                        loginViewModel = loginViewModel,
                        taskViewModel = taskViewModel,
                        groupViewModel = groupViewModel,
//                        profileViewModel = profileViewModel,
                        userViewModel = userViewModel,
                        topBarTitle = topBarTitle,
                        innerPadding = innerPadding
                    )















//                    NavHost(
//                        navController = navController,
//                        startDestination = if (authState == AuthState.LoggedIn) NavItem.Tasks.route else "login"
//                    ){
//                        composable("login") {
//
//                            LoginScreen(
//                                viewModel = loginViewModel,
//                                onLoginSuccess = {
////                                    navController.navigate(NavItem.Tasks.route) {
////                                        popUpTo("login") { inclusive = true }
////                                        launchSingleTop = true
////                                    }
//                                    authViewModel.loginSuccess()
//                                },
//                                onRegisterClick = {
//                                    navController.navigate("register")
//                                }
//                            )
//                        }
//
//                        // ======================================================================== //
//
//                        composable(NavItem.Tasks.route) {
//                            LaunchedEffect(Unit) {
//                                topBarTitle.value = "My Tasks"
//                            }
//                            TaskScreen(
//                                viewModel = taskViewModel,
//                                modifier = Modifier.padding(innerPadding),
//                                onTaskClick = { taskId ->
//                                    navController.navigate("taskDetail/$taskId")
//                                }
//                            )
//                        }
//
//                        // ======================================================================== //
//
//                        composable(
//                            route = "taskDetail/{taskId}",
//                            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
//                        ) { backStackEntry ->
//
//                            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
//
//                            LaunchedEffect(taskId) {
//                                topBarTitle.value = "Task Details"
//                            }
//
//                            TaskDetailsScreen(
//                                taskId = taskId,
//                                modifier = Modifier.padding(innerPadding),
//                                viewModel = taskViewModel,
//                                onEditTaskClick = { navController.navigate("editTask/$taskId")},
//                                onAssignUsersClick = { navController.navigate("editAssignedUsers/$taskId")},
//                                onBackClick = { navController.popBackStack() },
//                            )
//                        }
//
//                        // ======================================================================== //
//
//                        composable(
//                            route = "createTask/{groupId}",
//                            arguments = listOf(
//                                navArgument("groupId") { type = NavType.IntType }
//                            )
//                        ) { backStackEntry ->
//                            val groupId = backStackEntry.arguments!!.getInt("groupId")
//
//                            LaunchedEffect(groupId) {
//                                topBarTitle.value = "Add New Task"
//                            }
//
//                        CreateTaskScreen(
//                            viewModel = taskViewModel,
//                            groupViewModel = groupViewModel,
//                            groupId = groupId,
//                            modifier = Modifier.padding(innerPadding),
//                            onBackClick = { navController.popBackStack() },
//                            )
//                        }
//
//                        // ======================================================================== //
//
//                        composable(
//                            route = "editTask/{taskId}",
//                            arguments = listOf(
//                                navArgument("taskId") { type = NavType.IntType }
//                            )
//                        ) { backStackEntry ->
//                            val taskId = backStackEntry.arguments!!.getInt("taskId")
//
//                            LaunchedEffect(taskId) {
//                                topBarTitle.value = "Edit Task Details"
//                                taskViewModel.selectTaskById(taskId)
//                            }
//
//                            UpdateTaskScreen(
//                                taskId = taskId,
//                                viewModel = taskViewModel,
//                                modifier = Modifier.padding(innerPadding),
//                                onBackClick = { navController.popBackStack() },
//                            )
//                        }
//
//                        // ======================================================================== //
//
//                        composable(
//                            route = "editAssignedUsers/{taskId}",
//                            arguments = listOf(
//                                navArgument("taskId") { type = NavType.IntType }
//                            )
//                        ) { backStackEntry ->
//                            val taskId = backStackEntry.arguments!!.getInt("taskId")
//
//                            LaunchedEffect(taskId) {
//                                topBarTitle.value = "Edit Assigned Users"
//                            }
//
//                            UpdateAssignedUsersScreen(
//                                taskId = taskId,
//                                viewModel = taskViewModel,
//                                groupViewModel = groupViewModel,
//                                modifier = Modifier.padding(innerPadding),
//                                onCancelClick = { navController.popBackStack() },
//                            )
//                        }
//
//                        // ======================================================================== //
//
//                        composable(NavItem.Groups.route) {
//
//                            LaunchedEffect(Unit) {
//                                topBarTitle.value = "My Groups"
//                            }
//
//                            GroupScreen(
//                                viewModel = groupViewModel,
//                                modifier = Modifier.padding(innerPadding),
//                                onGroupClick = { groupId ->
//                                    navController.navigate("groupDetail/$groupId")
//                                },
//                                onAddGroupClick = {
//                                    navController.navigate("createGroup")
//                                }
//                            )
//                        }
//
//                        // ======================================================================== //
//
//                        composable(
//                            route = "groupDetail/{groupId}",
//                            arguments = listOf(navArgument("groupId") { type = NavType.IntType })
//                        ) {
//                            backStackEntry ->
//                            val groupId = backStackEntry.arguments?.getInt("groupId") ?: 0
//
//                            LaunchedEffect(groupId) {
//                                topBarTitle.value = "Group Details"
//                            }
//
//                            GroupDetailsScreen(
//                                groupId = groupId,
//                                viewModel = groupViewModel,
//                                modifier = Modifier.padding(innerPadding),
//                                onTaskClick = { taskId ->
//                                    navController.navigate("taskDetail/$taskId")
//                                },
//                                onAddTaskClick = { navController.navigate("createTask/$groupId")}
//                            )
//                        }
//
//                        // ======================================================================== //
//
//                        composable( route = "createGroup"
//                        ) { backStackEntry ->
//
//                            LaunchedEffect(backStackEntry) {
//                                topBarTitle.value = "Create New Group"
//                            }
//
//                            CreateGroupScreen(
//                                viewModel = groupViewModel,
//                                userViewModel = userViewModel,
//                                modifier = Modifier.padding(innerPadding),
//                                onBackClick = { navController.popBackStack() },
//                            )
//                        }
//
//                        // ======================================================================== //
//
//                        composable(NavItem.Profile.route) {
//
//                            LaunchedEffect(Unit) {
//                                topBarTitle.value = "Profile"
//                            }
//
//                            ProfileScreen(
//                                viewModel = profileViewModel,
//                                modifier = Modifier.padding(innerPadding),
//                                onLogout = {
//                                    authViewModel.logout()
//                                }
//                            )
//                        }
//
//                        // ======================================================================== //
//                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default_channel",
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.setDescription("Default channel for app notifications")

            val manager = getSystemService<NotificationManager?>(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}