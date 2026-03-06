package org.russel.komandoandroid.ui.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.ui.task.TaskList
import org.russel.komandoandroid.R
import org.russel.komandoandroid.ui.component.AppFloatingActionButton
import org.russel.komandoandroid.ui.user.UserList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsScreen(
    groupId: Int,
    viewModel: GroupViewModel,
    onBackClick: () -> Unit,
    onTaskClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onAddTaskClick: () -> Unit
) {
    val group by viewModel.selectedGroup.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val users by viewModel.users.collectAsState()

    LaunchedEffect(groupId) {
        viewModel.fetchGroupById(groupId)
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top),
        topBar = {
            TopAppBar(
                title = { Text("Group Details") },
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
        },
        floatingActionButton = {
            AppFloatingActionButton(
                onClick = onAddTaskClick,
                content = {
                    Icon(
                        painter = painterResource(R.drawable.ic_addtask),
                        contentDescription = "Add Task"
                    )
                }
            )
        }
    ) { innerPadding ->

        group?.let {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)

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
                            Row(verticalAlignment = Alignment.CenterVertically,
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
                                modifier = modifier.fillMaxWidth(),
                                onTaskClick = onTaskClick
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
                                users = users,
                                modifier = modifier.fillMaxWidth()
                            )
                        }



                    }
                }

            }
        }
    }
}