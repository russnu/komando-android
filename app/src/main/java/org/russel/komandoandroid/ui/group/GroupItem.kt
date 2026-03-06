package org.russel.komandoandroid.ui.group

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.R
import org.russel.komandoandroid.data.model.Group
import org.russel.komandoandroid.ui.component.CreatorBadge

@Composable
fun GroupItem(group: Group, currentUserId: Int?, onClick: () -> Unit) {
    val isCreator = group.createdBy.id == currentUserId
    val creatorDisplayName = if (isCreator) "You" else group.createdBy.fullName
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth()) {

                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tasks),
                            contentDescription = "Task Icon",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = group.taskCount.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    VerticalDivider(
                        modifier = Modifier.height(24.dp),
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.width(24.dp))

                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_users),
                            contentDescription = "User Icon",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = group.userCount.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    CreatorBadge(text = creatorDisplayName, isCurrentUser = isCreator)


                }

            }
        }
    }
}