package org.russel.komandoandroid.ui.task

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.ui.viewmodel.TaskViewModel

@Composable
fun TaskScreen(viewModel: TaskViewModel, modifier: Modifier = Modifier, onTaskClick: (Int) -> Unit) {

    val tasks by viewModel.tasks.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTasksByUser()
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            TaskList(
                tasks = tasks,
                onTaskClick = onTaskClick,
                modifier = Modifier.fillMaxSize(),
                currentUserId = currentUserId
            )
        }

    }



}