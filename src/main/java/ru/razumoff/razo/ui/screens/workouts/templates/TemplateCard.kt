package ru.razumoff.razo.ui.screens.workouts.templates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.razumoff.razo.R
import ru.razumoff.razo.models.WorkoutTemplate

@Composable
fun TemplateCard(
    template: WorkoutTemplate,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = template.name,
                    style = MaterialTheme.typography.titleMedium
                )

                template.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = pluralStringResource(
                        R.plurals.exercises_count,
                        template.exercises.size,
                        template.exercises.size
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_arrow_right),
                    contentDescription = "Перейти в шаблон",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
