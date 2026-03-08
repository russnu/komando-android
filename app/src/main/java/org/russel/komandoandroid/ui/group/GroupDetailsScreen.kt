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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.ui.task.TaskList
import org.russel.komandoandroid.R
import org.russel.komandoandroid.ui.component.AppFloatingActionButton
import org.russel.komandoandroid.ui.user.UserList
import org.russel.komandoandroid.ui.viewmodel.GroupViewModel

@Composable
fun GroupDetailsScreen(
    groupId: Int,
    viewModel: GroupViewModel,
    onTaskClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onAddTaskClick: () -> Unit
) {
    val group by viewModel.selectedGroup.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val members by viewModel.members.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()

    LaunchedEffect(groupId) {
        viewModel.fetchGroupById(groupId)
    }

    group?.let {

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
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )

                        )
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
            }

            AppFloatingActionButton(
                onClick = onAddTaskClick,
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                content = {
                    Icon(
                        painter = painterResource(R.drawable.ic_task_plus),
                        contentDescription = "Add Group"
                    )
                }
            )
        }

    }


}