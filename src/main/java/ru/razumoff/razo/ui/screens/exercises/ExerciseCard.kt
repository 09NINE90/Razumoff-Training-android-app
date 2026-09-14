package ru.razumoff.razo.ui.screens.exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.razumoff.razo.models.Exercise
import ru.razumoff.razo.models.localizedName
import ru.razumoff.razo.ui.components.cards.SurfaceCard
import ru.razumoff.razo.ui.screens.exercises.components.SurfaceChip

@Composable
fun ExerciseCard(
    exercise: Exercise,
    modifier: Modifier = Modifier
) {
    SurfaceCard(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleLarge
            )

            if (exercise.description.isNotEmpty()) {
                Text(
                    text = exercise.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Используем Row с Surface вместо Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Часть тела
                SurfaceChip(
                    text = exercise.bodyPart.localizedName()
                )

                // Тип движения
                SurfaceChip(
                    text = exercise.movementType.localizedName()
                )

                // Оборудование (если есть)
                if (exercise.equipment != null) {
                    SurfaceChip(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        text = exercise.equipment
                    )
                }
            }

            // Группы мышц
            if (exercise.muscleGroups.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    exercise.muscleGroups.take(3).forEach { muscle ->
                        SurfaceChip(
                            textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            text = muscle.localizedName()
                        )
                    }
                    if (exercise.muscleGroups.size > 3) {
                        SurfaceChip(
                            textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            text = "+${exercise.muscleGroups.size - 3}"
                        )
                    }
                }
            }
        }
    }
}
