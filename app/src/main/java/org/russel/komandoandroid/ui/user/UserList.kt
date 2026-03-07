package org.russel.komandoandroid.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.data.model.User

@Composable
fun UserList(users: List<User>, modifier: Modifier ) {
    if (users.isEmpty()) {
        Text(
            text = "No users available",
            modifier = modifier.padding(16.dp)
        )
    } else {
        Column (
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)) {

            users.forEach { user ->
                UserItem(user = user)
            }
        }
    }
}