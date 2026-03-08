package org.russel.komandoandroid.ui.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.ui.task.TaskList
import org.russel.komandoandroid.R
import org.russel.komandoandroid.ui.component.AppFloatingActionButton
import org.russel.komandoandroid.ui.component.AppOutlinedButton
import org.russel.komandoandroid.ui.component.ConfirmDeleteDialog
import org.russel.komandoandroid.ui.user.UserList
import org.russel.komandoandroid.ui.viewmodel.GroupViewModel

@Composable
fun GroupDetailsScreen(
    groupId: Int,
    viewModel: GroupViewModel,
    onTaskClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onAddTaskClick: () -> Unit,
    onEditMembersClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val group by viewModel.selectedGroup.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val members by viewModel.members.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()
    val isCreator = group?.createdBy?.id == currentUserId

    var isEditing by remember { mutableStateOf(false) }
    val groupName = rememberTextFieldState(group?.name ?: "")

    val focusRequester = remember { FocusRequester() }

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(groupId) {
        viewModel.fetchGroupById(groupId)
    }
    LaunchedEffect(group?.name) {
        groupName.setTextAndPlaceCursorAtEnd(group?.name ?: "")

    }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            focusRequester.requestFocus()
        }
    }

    group?.let { currentGroup ->

        Box(modifier = modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_group),
                            contentDescription = "Group Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        if (isEditing){
                            BasicTextField(
                                state = groupName,
                                textStyle = MaterialTheme.typography.titleLarge.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.weight(1f).focusRequester(focusRequester)
                            )
                        } else {
                            Text(
                                text = currentGroup.name,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        if (isCreator) {
                            if (isEditing) {
                                IconButton(
                                    enabled = groupName.text.toString() != group?.name,
                                    onClick = {
                                        viewModel.updateGroup(
                                            groupId,
                                            groupName.text.toString()
                                        )
                                        isEditing = false
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_check),
                                        contentDescription = "Save"
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        groupName.setTextAndPlaceCursorAtEnd(group?.name ?: "")
                                        isEditing = false
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_close),
                                        contentDescription = "Cancel"
                                    )
                                }

                            } else {
                                IconButton(
                                    onClick = {
                                        isEditing = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Name"
                                    )
                                }

                            }
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_tasks),
                                    contentDescription = "Task Icon",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Tasks",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )

                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            TaskList(
                                tasks = tasks,
                                modifier = Modifier.fillMaxWidth(),
                                onTaskClick = onTaskClick,
                                currentUserId = currentUserId
                            )
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
                                    text = "Members",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )

                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            UserList(
                                users = members,
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

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppFloatingActionButton(
                    onClick = onAddTaskClick,
                    content = {
                        Icon(
                            painter = painterResource(R.drawable.ic_task_plus),
                            contentDescription = "Add Group"
                        )
                    }
                )

                if (isCreator) {
                    AppFloatingActionButton(
                        onClick = onEditMembersClick,
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
                    itemName = currentGroup.name,
                    onConfirm = {
                        currentGroup.id?.let {
                            viewModel.deleteGroup(it)
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