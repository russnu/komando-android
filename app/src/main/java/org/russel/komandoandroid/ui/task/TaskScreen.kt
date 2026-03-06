package org.russel.komandoandroid.ui.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel,  modifier: Modifier = Modifier, onTaskClick: (Int) -> Unit) {
    val tasks by viewModel.tasks.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchTasksByUser()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Tasks") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(16.dp)){
            TaskList(
                tasks = tasks,
                modifier = modifier.padding(innerPadding),
                onTaskClick = onTaskClick
            )
        }


    }
}