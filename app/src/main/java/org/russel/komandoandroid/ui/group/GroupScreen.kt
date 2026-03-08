package org.russel.komandoandroid.ui.group

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.R
import org.russel.komandoandroid.ui.component.AppFloatingActionButton
import org.russel.komandoandroid.ui.viewmodel.GroupViewModel

@Composable
fun GroupScreen(viewModel: GroupViewModel, modifier: Modifier = Modifier, onGroupClick: (Int) -> Unit, onAddGroupClick: () -> Unit) {
    val groups by viewModel.groups.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()

    LaunchedEffect(currentUserId) {
        viewModel.fetchGroupsByUser()
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item {
                GroupList(
                    groups = groups,
                    modifier = Modifier.fillMaxSize(),
                    currentUserId = currentUserId,
                    onGroupClick = onGroupClick
                )
            }
        }



        AppFloatingActionButton(
            onClick = onAddGroupClick,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            content = {
                Icon(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = "Add Group"
                )
            }
        )
    }


}