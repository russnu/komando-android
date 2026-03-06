package org.russel.komandoandroid.ui.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.data.model.Group

@Composable
fun GroupList(groups: List<Group>, currentUserId: Int?, modifier: Modifier, onGroupClick: (Int) -> Unit  ) {
    if (groups.isEmpty()) {
        Text(
            text = "No groups available",
            modifier = modifier.padding(16.dp)
        )
    } else {
        LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(groups) { group ->
                GroupItem(
                    group = group,
                    currentUserId = currentUserId,
                    onClick = { group.id?.let { id -> onGroupClick(id) } }
                )
            }
        }
    }
}