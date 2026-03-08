package org.russel.komandoandroid.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.russel.komandoandroid.ui.viewmodel.AuthViewModel
import org.russel.komandoandroid.data.model.NavItem
import org.russel.komandoandroid.ui.group.CreateGroupScreen
import org.russel.komandoandroid.ui.group.GroupDetailsScreen
import org.russel.komandoandroid.ui.group.GroupScreen
import org.russel.komandoandroid.ui.group.UpdateMembersScreen
import org.russel.komandoandroid.ui.viewmodel.GroupViewModel
import org.russel.komandoandroid.ui.profile.ProfileScreen
import org.russel.komandoandroid.ui.task.CreateTaskScreen
import org.russel.komandoandroid.ui.task.TaskDetailsScreen
import org.russel.komandoandroid.ui.task.TaskScreen
import org.russel.komandoandroid.ui.viewmodel.TaskViewModel
import org.russel.komandoandroid.ui.task.UpdateAssignedUsersScreen
import org.russel.komandoandroid.ui.task.UpdateTaskScreen
import org.russel.komandoandroid.ui.viewmodel.UserViewModel

fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
    taskViewModel: TaskViewModel,
    groupViewModel: GroupViewModel,
    userViewModel: UserViewModel,
    authViewModel: AuthViewModel,
    topBarTitle: MutableState<String>,
    innerPadding: PaddingValues
) {

    navigation(
        startDestination = NavItem.Tasks.route,
        route = "main"
    ) {
        //-------------------------------------------------------------------------//
        composable(NavItem.Tasks.route) {

            LaunchedEffect(Unit) {
                topBarTitle.value = "My Tasks"
            }

            TaskScreen(
                viewModel = taskViewModel,
                modifier = Modifier.padding(innerPadding),
                onTaskClick = { taskId -> navController.navigate("tasks/$taskId/details") },
            )
        }
        //-------------------------------------------------------------------------//
        composable(
            route = "tasks/{taskId}/details",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->

            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0

            LaunchedEffect(taskId) {
                topBarTitle.value = "Task Details"
            }

            TaskDetailsScreen(
                taskId = taskId,
                modifier = Modifier.padding(innerPadding),
                viewModel = taskViewModel,
                onEditTaskClick = { navController.navigate("tasks/$taskId/edit")},
                onAssignUsersClick = { navController.navigate("tasks/$taskId/editAssignedUsers")},
                onBackClick = { navController.popBackStack() },
            )
        }
        //-------------------------------------------------------------------------//
        composable(
            route = "tasks/create/group/{groupId}",
            arguments = listOf(
                navArgument("groupId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments!!.getInt("groupId")

            LaunchedEffect(groupId) {
                topBarTitle.value = "Add New Task"
            }

            CreateTaskScreen(
                viewModel = taskViewModel,
                groupViewModel = groupViewModel,
                groupId = groupId,
                modifier = Modifier.padding(innerPadding),
                onBackClick = { navController.popBackStack() },
                )
        }
        //-------------------------------------------------------------------------//
        composable(
            route = "tasks/{taskId}/edit",
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments!!.getInt("taskId")

            LaunchedEffect(taskId) {
                topBarTitle.value = "Edit Task Details"
                taskViewModel.selectTaskById(taskId)
            }

            UpdateTaskScreen(
                taskId = taskId,
                viewModel = taskViewModel,
                modifier = Modifier.padding(innerPadding),
                onBackClick = { navController.popBackStack() },
            )
        }
        //-------------------------------------------------------------------------//
        composable(
            route = "tasks/{taskId}/editAssignedUsers",
            arguments = listOf(
                navArgument("taskId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments!!.getInt("taskId")

            LaunchedEffect(taskId) {
                topBarTitle.value = "Edit Assigned Users"
            }

            UpdateAssignedUsersScreen(
                taskId = taskId,
                viewModel = taskViewModel,
                groupViewModel = groupViewModel,
                modifier = Modifier.padding(innerPadding),
                onCancelClick = { navController.popBackStack() },
            )
        }
        
        // ================================================================================================= //
        
        composable(NavItem.Groups.route) {

            LaunchedEffect(Unit) {
                topBarTitle.value = "My Groups"
            }

            GroupScreen(
                viewModel = groupViewModel,
                modifier = Modifier.padding(innerPadding),
                onGroupClick = { groupId -> navController.navigate("groups/$groupId/details") },
                onAddGroupClick = { navController.navigate("groups/create") }
            )
        }
        //-------------------------------------------------------------------------//
        composable(
            route = "groups/{groupId}/details",
            arguments = listOf(navArgument("groupId") { type = NavType.IntType })
        ) {
                backStackEntry ->
            val groupId = backStackEntry.arguments?.getInt("groupId") ?: 0

            LaunchedEffect(groupId) {
                topBarTitle.value = "Group Details"
            }

            GroupDetailsScreen(
                groupId = groupId,
                viewModel = groupViewModel,
                modifier = Modifier.padding(innerPadding),
                onTaskClick = { taskId -> navController.navigate("tasks/$taskId/details")
                },
                onAddTaskClick = { navController.navigate("tasks/create/group/$groupId")},
                onEditMembersClick = { navController.navigate("groups/$groupId/members/edit") },
                onBackClick = { navController.popBackStack() }
            )
        }
        //-------------------------------------------------------------------------//
        composable( route = "groups/create"
        ) { backStackEntry ->

            LaunchedEffect(backStackEntry) {
                topBarTitle.value = "Create New Group"
            }

            CreateGroupScreen(
                viewModel = groupViewModel,
                userViewModel = userViewModel,
                modifier = Modifier.padding(innerPadding),
                onBackClick = { navController.popBackStack() },
            )
        }
        //-------------------------------------------------------------------------//
        composable(
            route = "groups/{groupId}/members/edit",
            arguments = listOf(
                navArgument("groupId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments!!.getInt("groupId")

            LaunchedEffect(groupId) {
                topBarTitle.value = "Edit Group Members"
            }

            UpdateMembersScreen(
                groupId = groupId,
                viewModel = groupViewModel,
                userViewModel = userViewModel,
                modifier = Modifier.padding(innerPadding),
                onCancelClick = { navController.popBackStack() },
            )
        }
        //-------------------------------------------------------------------------//
        composable(NavItem.Profile.route) {

            LaunchedEffect(Unit) {
                topBarTitle.value = "Profile"
            }

            ProfileScreen(
                viewModel = authViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}