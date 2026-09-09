package ru.razumoff.razumofftraining.ui.screens.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.razumoff.razumofftraining.models.WorkoutSession
import ru.razumoff.razumofftraining.ui.components.headers.IslandWithButtonHeader
import ru.razumoff.razumofftraining.ui.screens.exercises.ExercisesCard
import ru.razumoff.razumofftraining.ui.screens.workouts.session.SessionCard
import ru.razumoff.razumofftraining.ui.screens.workouts.templates.TemplatesCard
import ru.razumoff.razumofftraining.utils.FormatUtils.workouts
import ru.razumoff.razumofftraining.ui.screens.exercises.ExerciseViewModel
import kotlin.collections.lastIndex

@Composable
fun WorkoutScreen(
    viewModelWorkout: WorkoutViewModel = viewModel(),
    viewModelExercise: ExerciseViewModel = viewModel(),
    onTemplatesClick: () -> Unit,
    onExerciseClick: () -> Unit,
    onSessionClick: (WorkoutSession) -> Unit,
    modifier: Modifier = Modifier
) {

    val exercisesCount by viewModelExercise.exercisesCount.collectAsState()
    val templatesCount by viewModelWorkout.templatesCount.collectAsState()
    val sessions by viewModelWorkout.sessions.collectAsState()
    val isInitialLoading by viewModelWorkout.isInitialLoading.collectAsState()
    val errorMessage by viewModelWorkout.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        if (isInitialLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Загрузка данных...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(70.dp))

                TemplatesCard(
                    templatesCount = templatesCount,
                    onClick = onTemplatesClick
                )
                Spacer(modifier = Modifier.height(12.dp))

                ExercisesCard(
                    exercisesCount = exercisesCount,
                    onClick = onExerciseClick
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Заголовок раздела "Последние тренировки"
                Text(
                    text = if (sessions.isEmpty()) {
                        "Последние тренировки"
                    } else {
                        "Последние ${sessions.size.workouts()}"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Список сессий
                if (sessions.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Нет выполненных тренировок",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Начните первую тренировку из шаблона",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        itemsIndexed(
                            items = sessions,
                            key = { _, item -> item.id }
                        ) { index, session ->
                            SessionCard(
                                session = session,
                                onClick = { onSessionClick(session) },
                                modifier = Modifier.padding(
                                    bottom = if (index == sessions.lastIndex) 110.dp else 0.dp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Плавающий заголовок
        IslandWithButtonHeader(
            headerText = "Тренировки",
            actionDescription = "Добавить",
            onActionClick = onTemplatesClick
        )
    }
}
