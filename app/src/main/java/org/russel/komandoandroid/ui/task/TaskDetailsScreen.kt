package org.russel.komandoandroid.ui.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.russel.komandoandroid.R
import org.russel.komandoandroid.ui.component.StatusDropdown
import org.russel.komandoandroid.ui.user.UserList
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.russel.komandoandroid.ui.component.AppFloatingActionButton
import org.russel.komandoandroid.ui.component.AppOutlinedButton
import org.russel.komandoandroid.ui.component.ConfirmDeleteDialog
import org.russel.komandoandroid.ui.component.CreatorBadge
import org.russel.komandoandroid.ui.viewmodel.TaskViewModel

@Composable
fun TaskDetailsScreen(
    taskId: Int,
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier,
    onEditTaskClick: () -> Unit,
    onAssignUsersClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val task by viewModel.selectedTask.collectAsState()
    val assignedUsers by viewModel.assignedUsers.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()
    val isUserAssigned = assignedUsers.any { it.id == currentUserId }

    val isCreator = task?.createdBy?.id == currentUserId
    val creatorDisplayName = if (isCreator) "You" else task?.createdBy?.fullName

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(taskId) {
        viewModel.fetchTaskById(taskId)
    }

    task?.let { currentTask ->
        Box(modifier = modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)

            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                            contentColor = MaterialTheme.colorScheme.onBackground,

                            ),
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Column(modifier = Modifier.padding(16.dp)){
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_tasks),
                                    contentDescription = "Task Icon",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = currentTask.title,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )

                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Description: ",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                ))
                            Text(currentTask.description,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontStyle = FontStyle.Italic
                                ))

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start) {
                                Text("Created by: ",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ))
                                Spacer(modifier = Modifier.width(8.dp))
                                CreatorBadge(text = creatorDisplayName, isCurrentUser = isCreator)
                            }
                        }
                    }
                }


                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Status",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        StatusDropdown(task = task, enabled = isUserAssigned) { status ->
                            viewModel.updateTaskStatus(taskId, status)
                        }
                    }
                }



                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_users),
                                    contentDescription = "Users Icon",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Assigned Users",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )

                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            UserList(
                                users = assignedUsers,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                if (isCreator){
                    item {
                        AppOutlinedButton(
                            text = "Delete",
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

            }

            if (isCreator) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AppFloatingActionButton(
                        onClick = onEditTaskClick,
                        modifier = Modifier,
                        content = {
                            Icon(
                                painter = painterResource(R.drawable.ic_edit_task),
                                contentDescription = "Edit Task"
                            )
                        }
                    )

                    AppFloatingActionButton(
                        onClick = onAssignUsersClick,
                        modifier = Modifier,
                        content = {
                            Icon(
                                painter = painterResource(R.drawable.ic_edit_user),
                                contentDescription = "Edit Task"
                            )
                        }
                    )
                }
            }


            if (showDeleteDialog) {
                ConfirmDeleteDialog(
                    itemName = currentTask.title,
                    onConfirm = {
                        currentTask.id?.let {
                            viewModel.deleteTask(it)
                            onBackClick()
                        }
                        showDeleteDialog = false
                    },
                    onDismiss = { showDeleteDialog = false }
                )
            }
        }

    }
}