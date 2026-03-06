package org.russel.komandoandroid.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.russel.komandoandroid.data.model.enums.TaskStatus
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import org.russel.komandoandroid.R

@Composable
fun CreatorBadge(
    text: String,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.labelMedium,

    ) {


    val containerColor = if (isCurrentUser)
        MaterialTheme.colorScheme.tertiary
    else
        MaterialTheme.colorScheme.secondary

    val contentColor = if (isCurrentUser)
        MaterialTheme.colorScheme.onTertiary
    else
        MaterialTheme.colorScheme.onSecondary

    Surface(
        shape = RoundedCornerShape(50),
        color = containerColor,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_creator),
                contentDescription = "User Icon",
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = textStyle,
                fontWeight = FontWeight.SemiBold,
                color = contentColor
            )
        }

    }
}