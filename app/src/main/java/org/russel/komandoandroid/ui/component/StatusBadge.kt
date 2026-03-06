package org.russel.komandoandroid.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.russel.komandoandroid.data.model.enums.TaskStatus
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun StatusBadge(
    status: TaskStatus,
    modifier: Modifier = Modifier,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.labelMedium,

) {
    val (containerColor, contentColor) = when (status) {
        TaskStatus.COMPLETED ->
            Color(0xFFE8F5E9) to Color(0xFF1B5E20)   // soft green bg, dark green text

        TaskStatus.IN_PROGRESS ->
            Color(0xFFE3F2FD) to Color(0xFF0D47A1)   // soft blue bg, dark blue text

        TaskStatus.PENDING ->
            Color(0xFFF5F5F5) to Color(0xFF616161)   // light gray bg, dark gray text

        else -> {
            MaterialTheme.colorScheme.surfaceVariant to
                    MaterialTheme.colorScheme.onSurfaceVariant
        }
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = containerColor,
        modifier = modifier
    ) {
        Text(
            text = status.displayName,
            style = textStyle,
            fontWeight = FontWeight.Bold,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}