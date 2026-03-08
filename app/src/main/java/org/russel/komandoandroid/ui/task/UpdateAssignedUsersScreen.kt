package org.russel.komandoandroid.ui.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.ui.component.AppButton
import org.russel.komandoandroid.ui.component.AppOutlinedButton
import org.russel.komandoandroid.ui.component.UserSelector
import org.russel.komandoandroid.ui.viewmodel.GroupViewModel
import org.russel.komandoandroid.ui.viewmodel.TaskViewModel

@Composable
fun UpdateAssignedUsersScreen(
    taskId: Int,
    viewModel: TaskViewModel,
    groupViewModel: GroupViewModel,
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit
) {
    val task by viewModel.selectedTask.collectAsState()
    val assignedUsers by viewModel.assignedUsers.collectAsState()
    val groupMembers by groupViewModel.members.collectAsState()

    LaunchedEffect(taskId) {
        viewModel.fetchTaskById(taskId)
    }

    LaunchedEffect(task?.group?.id) {
        task?.group?.id?.let {
            groupViewModel.fetchGroupById(it)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)

    ) {
        item {
            UserSelector(
                users = groupMembers,
                selectedUsers = assignedUsers.toSet(),
                onUserToggle = { user ->
                    viewModel.toggleAssignedUser(user)
                }
            )
        }

        item {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AppButton(
                    text = "Update Assigned Users",
                    onClick = {
                        viewModel.updateTaskAssignments()
                        onCancelClick()
                    },
                    modifier = Modifier,
                )

                AppOutlinedButton(
                    text = "Cancel",
                    onClick = onCancelClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }



        }
    }
}