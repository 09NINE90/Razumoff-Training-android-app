package ru.razumoff.razo.ui.screens.workouts.sessions.details.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.razumoff.razo.R
import ru.razumoff.razo.models.WorkoutSession
import ru.razumoff.razo.utils.WorkoutFormatter
import ru.razumoff.razo.utils.copyToClipboard

@Composable
fun SessionInfoDropdown(
    session: WorkoutSession,
    expanded: Boolean,
    onDismissRequest: () -> Unit
){

    val context = LocalContext.current
    val formatter = remember(context) {
        WorkoutFormatter(context)
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    stringResource(R.string.copy_md)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        R.drawable.ic_copy_simple
                    ),
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
            },
            onClick = {
                val markdown = formatter.formatToMarkdown(session)
                copyToClipboard(
                    context,
                    markdown
                )
                onDismissRequest()
            }
        )

        DropdownMenuItem(
            text = {
                Text(
                    stringResource(R.string.copy_txt)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        R.drawable.ic_copy_simple
                    ),
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
            },
            onClick = {
                val text = formatter.formatToSimpleText(session)
                copyToClipboard(
                    context,
                    text
                )
                onDismissRequest()
            }
        )
    }

}

