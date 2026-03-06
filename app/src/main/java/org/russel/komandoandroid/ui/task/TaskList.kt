package org.russel.komandoandroid.ui.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.data.model.Task
import androidx.compose.foundation.lazy.items

@Composable
fun TaskList(tasks: List<Task>, modifier: Modifier, onTaskClick: (Int) -> Unit  ) {
    if (tasks.isEmpty()) {
        Text(
            text = "No tasks available",
            modifier = modifier.padding(16.dp)
        )
    } else {
        Column (modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            tasks.forEach { task ->
                TaskItem(task = task, onClick = { task.id?.let(onTaskClick) })
            }
        }
    }
}