package org.russel.komandoandroid.ui.task

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.ui.form.UpdateTaskForm

@Composable
fun UpdateTaskScreen(
    taskId: Int,
    viewModel: TaskViewModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    val selectedTask by viewModel.selectedTask.collectAsState()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(taskId) {
        viewModel.fetchTaskById(taskId)
    }

    LaunchedEffect(selectedTask) {
        selectedTask?.let {
            title = it.title
            description = it.description
        }
    }

    LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = PaddingValues(start = 32.dp, end= 32.dp, bottom = 32.dp)) {
        item {
            UpdateTaskForm(
                title = title,
                onTitleChange = { title = it },
                description = description,
                onDescriptionChange = { description = it },
                onUpdateClick = {
                    viewModel.updateTask(title, description)
                    viewModel.clearAssignedUsers()
                    onBackClick()
                },
                onCancelClick = {
                    viewModel.clearAssignedUsers()
                    onBackClick()
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}