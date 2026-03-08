package org.russel.komandoandroid.ui.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.data.model.User
import org.russel.komandoandroid.ui.component.AppButton
import org.russel.komandoandroid.ui.component.AppOutlinedButton
import org.russel.komandoandroid.ui.component.AppTextField
import org.russel.komandoandroid.ui.component.UserSelector

@Composable
fun CreateGroupForm(
    name: String,
    onNameChange: (String) -> Unit,
    users: List<User>,
    selectedUsers: Set<User>,
    onUserToggle: (User) -> Unit,
    onCreateClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppTextField(
            value = name,
            onValueChange = onNameChange,
            label = "Group Name"
        )

        Text(
            text = "Add Members",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        UserSelector(
            users = users,
            selectedUsers = selectedUsers,
            onUserToggle = onUserToggle
        )

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            text = "Create Group",
            onClick = onCreateClick,
            modifier = modifier,
            enabled = name.isNotBlank()
        )

        AppOutlinedButton(
            text = "Cancel",
            onClick = onCancelClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}