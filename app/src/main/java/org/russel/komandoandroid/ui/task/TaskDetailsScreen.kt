package org.russel.komandoandroid.ui.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.russel.komandoandroid.R
import org.russel.komandoandroid.ui.component.StatusDropdown
import org.russel.komandoandroid.ui.user.UserList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    taskId: Int,
    viewModel: TaskViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val task by viewModel.selectedTask.collectAsState()
    val assignedUsers by viewModel.assignedUsers.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()
    val isUserAssigned = assignedUsers.any { it.id == currentUserId }
    LaunchedEffect(taskId) {
        viewModel.fetchTaskById(taskId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Details") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        task?.let {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)

            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = modifier.fillMaxWidth()
                    ){
                        Column(modifier = Modifier.padding(16.dp)){
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_tasks),
                                    contentDescription = "Group Icon",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = it.title,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontWeight = FontWeight.Bold
                                    )

                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Description: ",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic
                                ))
                            Text(it.description, color = MaterialTheme.colorScheme.onBackground)

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
                                modifier = modifier.fillMaxWidth()
                            )
                        }



                    }
                }


            }
        }
    }
}