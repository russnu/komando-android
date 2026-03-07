package org.russel.komandoandroid.ui.task

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.russel.komandoandroid.ui.form.CreateTaskForm
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.ui.group.GroupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    viewModel: TaskViewModel,
    groupViewModel: GroupViewModel,
    groupId: Int,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val assignedUsers by viewModel.assignedUsers.collectAsState()
    val members by groupViewModel.members.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAllUsers()
    }

    LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = PaddingValues(start = 32.dp, end= 32.dp, bottom = 32.dp)) {
        item {
            CreateTaskForm(
                title = title,
                onTitleChange = { title = it },
                description = description,
                onDescriptionChange = { description = it },
                users = members,
                selectedUsers = assignedUsers.toSet(),
                onUserToggle = { user ->
                    viewModel.toggleAssignedUser(user)
                },
                onCreateClick = {
                    viewModel.addTask(title, description, groupId)
                    onBackClick()



                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}