package org.russel.komandoandroid.ui.task

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.russel.komandoandroid.data.model.User
import org.russel.komandoandroid.ui.form.CreateTaskForm
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    viewModel: TaskViewModel,
    groupId: Int,
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedUsers by remember { mutableStateOf(setOf<User>()) }
    val users by viewModel.users.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAllUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Task") },
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
    ) { padding ->

        CreateTaskForm(
            title = title,
            onTitleChange = { title = it },
            description = description,
            onDescriptionChange = { description = it },
            users = users,
            selectedUsers = selectedUsers,
            onUserToggle = { user ->
                selectedUsers = if (selectedUsers.contains(user)) {
                    selectedUsers - user
                } else {
                    selectedUsers + user
                }
            },
            onCreateClick = {
                viewModel.addTask(title, description, groupId)
                onBackClick()
            },
            modifier = Modifier.padding(padding)
        )
    }
}