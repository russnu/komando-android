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
import org.russel.komandoandroid.ui.component.AppButton
import org.russel.komandoandroid.ui.component.AppOutlinedButton
import org.russel.komandoandroid.ui.component.AppTextField

@Composable
fun UpdateTaskForm(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onUpdateClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppTextField(
            value = title,
            onValueChange = onTitleChange,
            label = "Title"
        )

        AppTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = "Description",
            singleLine = false,
            minLines = 4
        )

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            text = "Save",
            onClick = onUpdateClick,
            modifier = modifier,
            enabled = title.isNotBlank()
        )

        AppOutlinedButton(
            text = "Cancel",
            onClick = onCancelClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}