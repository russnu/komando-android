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
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.messaging.FirebaseMessaging
import org.russel.komandoandroid.data.auth.SessionManager
import org.russel.komandoandroid.data.remote.RetrofitClient
import org.russel.komandoandroid.data.repository.AuthRepository
import org.russel.komandoandroid.data.repository.DeviceRepository
import org.russel.komandoandroid.data.repository.GroupRepository
import org.russel.komandoandroid.data.repository.TaskRepository
import org.russel.komandoandroid.data.repository.UserRepository
import org.russel.komandoandroid.fcmservice.FcmTopicManager
import org.russel.komandoandroid.ui.component.AppTopBar
import org.russel.komandoandroid.ui.component.NavItem
import org.russel.komandoandroid.ui.factory.GroupViewModelFactory
import org.russel.komandoandroid.ui.factory.LoginViewModelFactory
import org.russel.komandoandroid.ui.factory.ProfileViewModelFactory
import org.russel.komandoandroid.ui.factory.TaskViewModelFactory
import org.russel.komandoandroid.ui.group.GroupDetailsScreen
import org.russel.komandoandroid.ui.group.GroupScreen
import org.russel.komandoandroid.ui.group.GroupViewModel
import org.russel.komandoandroid.ui.login.LoginScreen
import org.russel.komandoandroid.ui.login.LoginViewModel
import org.russel.komandoandroid.ui.profile.ProfileScreen
import org.russel.komandoandroid.ui.profile.ProfileViewModel
import org.russel.komandoandroid.ui.task.CreateTaskScreen
import org.russel.komandoandroid.ui.task.TaskDetailsScreen
import org.russel.komandoandroid.ui.task.TaskScreen
import org.russel.komandoandroid.ui.task.TaskViewModel
import org.russel.komandoandroid.ui.theme.KomandoandroidTheme


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
                val profileViewModel: ProfileViewModel = viewModel(
                    factory = ProfileViewModelFactory(sessionManager)
                )
                val groupViewModel: GroupViewModel = viewModel(
                    factory = GroupViewModelFactory(groupRepository, sessionManager)
                )

                val loginViewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(authRepository, deviceRepository, groupViewModel , sessionManager)
                )


                // ======================================================================== //

                val navController = rememberNavController()
                val items = listOf(
                    NavItem.Tasks,
                    NavItem.Groups,
                    NavItem.Profile
                )
                val startDestination = if (sessionManager.isLoggedIn()) NavItem.Tasks.route else "login"

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
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            val currentRoute =
                                navController.currentBackStackEntryAsState().value?.destination?.route

                            /* TODO: implement hiding bottom bars on login page :
                            *    bottomBar = {
                            *       if (showBottomBar) {
                            *           NavigationBar { ... }
                            *       }
                            *   } */
                            val showBottomBar = currentRoute in listOf(
                                NavItem.Tasks.route,
                                NavItem.Groups.route,
                                NavItem.Profile.route
                            )

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
                    }) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ){
                        composable("login") {
                            LoginScreen(
                                viewModel = loginViewModel,
                                onLoginSuccess = {
                                    navController.navigate(NavItem.Tasks.route) {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onRegisterClick = {
                                    navController.navigate("register")
                                }
                            )
                        }

                        // ======================================================================== //

                        composable(NavItem.Tasks.route) {
                            LaunchedEffect(Unit) {
                                topBarTitle.value = "My Tasks"
                            }
                            TaskScreen(
                                viewModel = taskViewModel,
                                modifier = Modifier.padding(innerPadding),
                                onTaskClick = { taskId ->
                                    navController.navigate("taskDetail/$taskId")
                                }
                            )
                        }

                        // ======================================================================== //

                        composable(
                            route = "taskDetail/{taskId}",
                            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                        ) { backStackEntry ->

                            LaunchedEffect(Unit) {
                                topBarTitle.value = "Task Details"
                            }

                            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0

                            TaskDetailsScreen(
                                taskId = taskId,
                                modifier = Modifier.padding(innerPadding),
                                viewModel = taskViewModel
                            )
                        }

                        // ======================================================================== //

                        composable(
                            route = "createTask/{groupId}",
                            arguments = listOf(
                                navArgument("groupId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val groupId = backStackEntry.arguments!!.getInt("groupId")

                            LaunchedEffect(groupId) {
                                topBarTitle.value = "Create Task"
                            }

                        CreateTaskScreen(
                            viewModel = taskViewModel,
                            groupViewModel = groupViewModel,
                            groupId = groupId,
                            modifier = Modifier.padding(innerPadding),
                            onBackClick = { navController.popBackStack() }
                            )
                        }

                        // ======================================================================== //

                        composable(NavItem.Groups.route) {

                            LaunchedEffect(Unit) {
                                topBarTitle.value = "My Groups"
                            }

                            GroupScreen(
                                viewModel = groupViewModel,
                                modifier = Modifier.padding(innerPadding),
                                onGroupClick = { groupId ->
                                    navController.navigate("groupDetail/$groupId")
                                },
                                onAddGroupClick = {
                                    navController.navigate("addGroup")
                                }
                            )
                        }

                        // ======================================================================== //

                        composable(
                            route = "groupDetail/{groupId}",
                            arguments = listOf(navArgument("groupId") { type = NavType.IntType })
                        ) {
                            backStackEntry ->
                            val groupId = backStackEntry.arguments?.getInt("groupId") ?: 0

                            LaunchedEffect(backStackEntry) {
                                topBarTitle.value = "Group Details"
                            }

                            GroupDetailsScreen(
                                groupId = groupId,
                                viewModel = groupViewModel,
                                modifier = Modifier.padding(innerPadding),
                                onTaskClick = { taskId ->
                                    navController.navigate("taskDetail/$taskId")
                                },
                                onAddTaskClick = { navController.navigate("createTask/$groupId")}
                            )
                        }

                        // ======================================================================== //

                        composable(NavItem.Profile.route) {

                            LaunchedEffect(Unit) {
                                topBarTitle.value = "Profile"
                            }

                            ProfileScreen(
                                viewModel = profileViewModel,
                                modifier = Modifier.padding(innerPadding),
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                    }
                                }
                            )
                        }

                        // ======================================================================== //
                    }
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