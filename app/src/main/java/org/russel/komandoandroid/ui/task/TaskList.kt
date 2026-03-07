package org.russel.komandoandroid.ui.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.data.model.Task

@Composable
fun TaskList(tasks: List<Task>, modifier: Modifier = Modifier, onTaskClick: (Int) -> Unit  ) {
    if (tasks.isEmpty()) {
        Text(
            text = "No tasks available",
            modifier = modifier
        )
    } else {
        Column (
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)) {

            tasks.forEach { task ->
                TaskItem(
                    task = task,
                    onClick = { task.id?.let(onTaskClick) }
                )
            }
        }
    }
}