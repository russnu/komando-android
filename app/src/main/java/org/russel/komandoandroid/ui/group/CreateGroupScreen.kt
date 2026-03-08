package org.russel.komandoandroid.ui.group

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.ui.form.CreateGroupForm
import org.russel.komandoandroid.ui.viewmodel.GroupViewModel
import org.russel.komandoandroid.ui.viewmodel.UserViewModel

@Composable
fun CreateGroupScreen(
    viewModel: GroupViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    val allUsers by userViewModel.users.collectAsState()
    val addedMembers by viewModel.addedMembers.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.fetchAllUsers()
    }

    LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = PaddingValues(start = 32.dp, end= 32.dp, bottom = 32.dp)) {
        item {
            CreateGroupForm(
                name = name,
                onNameChange = { name = it },
                users = allUsers,
                selectedUsers = addedMembers,
                onUserToggle = { user ->
                    viewModel.toggleAddedUsers(user)
                },
                onCreateClick = {
                    viewModel.addGroup(name = name)
                    viewModel.clearAddedMembers()
                    onBackClick()
                },
                onCancelClick = {
                    viewModel.clearAddedMembers()
                    onBackClick()
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}