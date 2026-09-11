package ru.razumoff.razo.ui.components.sections

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        modifier = modifier
    )
}