package ru.razumoff.razumofftraining.ui.components.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActionButtons(
    onCancel: () -> Unit,
    onSave: () -> Unit,
    isSaveEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    cancelText: String = "Отмена",
    saveText: String = "Сохранить"
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onCancel,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Text(cancelText)
        }

        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f),
            enabled = isSaveEnabled
        ) {
            Text(saveText)
        }
    }
}