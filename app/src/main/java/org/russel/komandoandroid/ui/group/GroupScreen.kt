package org.russel.komandoandroid.ui.group

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.russel.komandoandroid.R
import org.russel.komandoandroid.ui.component.AppFloatingActionButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(viewModel: GroupViewModel,  modifier: Modifier = Modifier, onGroupClick: (Int) -> Unit,  onAddGroupClick: () -> Unit) {
    val groups by viewModel.groups.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchGroupsByUser()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Groups") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
            )
        },
        floatingActionButton = {
            AppFloatingActionButton(
                onClick = onAddGroupClick,
                content = {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = "Add Task"
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(16.dp)){
            GroupList(
                groups = groups,
                modifier = modifier.padding(innerPadding),
                currentUserId = currentUserId,
                onGroupClick = onGroupClick
            )
        }


    }
}