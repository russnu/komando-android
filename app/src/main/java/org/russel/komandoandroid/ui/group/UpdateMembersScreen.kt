package org.russel.komandoandroid.ui.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.ui.component.AppButton
import org.russel.komandoandroid.ui.component.AppOutlinedButton
import org.russel.komandoandroid.ui.component.UserSelector
import org.russel.komandoandroid.ui.viewmodel.GroupViewModel
import org.russel.komandoandroid.ui.viewmodel.UserViewModel

@Composable
fun UpdateMembersScreen(
    groupId: Int,
    viewModel: GroupViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit
) {
    val group by viewModel.selectedGroup.collectAsState()
    val members by viewModel.members.collectAsState()
    val selectedMembers by viewModel.addedMembers.collectAsState()
    val allUsers by userViewModel.users.collectAsState()

    LaunchedEffect(groupId) {
        viewModel.fetchGroupById(groupId)
        userViewModel.fetchAllUsers()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)

    ) {
        item {
            UserSelector(
                users = allUsers,
                selectedUsers = selectedMembers,
                onUserToggle = { user ->
                    viewModel.toggleAddedMember(user)
                }
            )
        }

        item {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)) {
                AppButton(
                    text = "Update Members",
                    onClick = {
                        viewModel.updateGroupMembers()
                        onCancelClick()
                    },
                    modifier = Modifier,
                )

                AppOutlinedButton(
                    text = "Cancel",
                    onClick = onCancelClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }



        }
    }
}