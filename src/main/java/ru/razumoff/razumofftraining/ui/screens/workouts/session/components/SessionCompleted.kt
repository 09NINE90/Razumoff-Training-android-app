package ru.razumoff.razumofftraining.ui.screens.workouts.session.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.razumoff.razumofftraining.ui.screens.workouts.session.SessionExerciseState
import ru.razumoff.razumofftraining.utils.FormatUtils.exercises
import ru.razumoff.razumofftraining.utils.FormatUtils.sets
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SessionCompleted(
    exercises: List<SessionExerciseState>,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalSets = exercises.sumOf { it.sets.size }
    val totalExercises = exercises.size

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 110.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Заголовок
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Завершено",
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Тренировка завершена!",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Отличная работа!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${totalExercises.exercises()} • ${totalSets.sets()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Список упражнений
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(exercises) { exercise ->
                    ExerciseSummaryCard(
                        exercise = exercise
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка "Закрыть"
            Button(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Закрыть")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
