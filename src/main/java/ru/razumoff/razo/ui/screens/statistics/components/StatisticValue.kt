package ru.razumoff.razo.ui.screens.statistics.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatisticValue(
    title: String,
    value: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
