package org.russel.komandoandroid.ui.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
            modifier = modifier
        )
    } else {
        Column (
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)) {

            groups.forEach { group ->
                GroupItem(
                    group = group,
                    currentUserId = currentUserId,
                    onClick = { group.id?.let { id -> onGroupClick(id) } }
                )
            }
        }
    }
}