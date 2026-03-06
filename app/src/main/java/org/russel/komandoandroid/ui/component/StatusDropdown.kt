package org.russel.komandoandroid.ui.component


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import org.russel.komandoandroid.data.model.Task
import org.russel.komandoandroid.data.model.enums.TaskStatus
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusDropdown(
    task: Task?,
    enabled: Boolean = true,
    onStatusSelected: (TaskStatus) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf(task?.status ?: TaskStatus.PENDING) }

    val statuses = listOf(
        TaskStatus.PENDING,
        TaskStatus.IN_PROGRESS,
        TaskStatus.COMPLETED
    )

    Box {

        StatusBadge(status = selectedStatus,
            modifier = Modifier.clickable(enabled = enabled) { expanded = !expanded }.alpha(if (enabled) 1f else 0.5f),
            textStyle = MaterialTheme.typography.titleMedium,)

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            statuses.forEachIndexed { index, status ->
                DropdownMenuItem(
                    text = { Text(status.displayName) },
                    onClick = {
                        selectedStatus = status
                        expanded = false
                        onStatusSelected(status)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                if (index < statuses.lastIndex) {
                    Divider()
                }
            }
        }
    }
}

