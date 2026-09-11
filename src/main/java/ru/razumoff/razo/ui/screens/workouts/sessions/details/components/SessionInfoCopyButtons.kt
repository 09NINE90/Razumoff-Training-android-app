package ru.razumoff.razo.ui.screens.workouts.sessions.details.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.razumoff.razo.R
import ru.razumoff.razo.models.WorkoutSession
import ru.razumoff.razo.utils.WorkoutFormatter

@Composable
fun SessionInfoCopyButtons(
    session: WorkoutSession?,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current

    val formatter = WorkoutFormatter(context)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                session?.let {
                    val markdown = formatter.formatToMarkdown(it)
                    copyToClipboard(context,markdown)
                }
            },
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
           Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_copy_simple),
                contentDescription = "Копировать MD",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("MD")
        }

        Button(
            onClick = {
                session?.let {
                    val text =  formatter.formatToSimpleText(it)
                    copyToClipboard(context, text)
                }
            },
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_copy_simple),
                contentDescription = "Копировать TXT",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("TXT")
        }
    }

}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Workout Results", text)
    clipboard.setPrimaryClip(clip)
}
