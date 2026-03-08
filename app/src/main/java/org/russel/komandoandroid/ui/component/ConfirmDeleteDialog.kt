package org.russel.komandoandroid.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    itemName: String = "this item"
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Confirm Delete") },
        text = { Text(text = "Are you sure you want to delete $itemName?") },
        confirmButton = {
            AppButton(
                text = "Delete",
                onClick = onConfirm,
                containerColor = MaterialTheme.colorScheme.error
            )
//                TextButton(onClick = onConfirm) {
//                    Text("Delete", color = MaterialTheme.colorScheme.error)
//                }
        },
        dismissButton = {
            AppOutlinedButton(
                text = "Cancel",
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
            )
//                TextButton(onClick = onDismiss) {
//                    Text("Cancel")
//                }
        }
    )
}