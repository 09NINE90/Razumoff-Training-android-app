package ru.razumoff.razo.ui.components.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrimaryText(
    text: String,
    horizontalPadding: Dp = 12.dp,
    verticalPadding: Dp = 4.dp,
    fontSize: TextUnit = 12.sp
) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding),
        fontSize = fontSize,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}