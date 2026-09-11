package ru.razumoff.razo.ui.screens.workouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.razumoff.razo.R
import ru.razumoff.razo.models.WorkoutSession
import ru.razumoff.razo.ui.components.headers.IslandWithButtonHeader
import ru.razumoff.razo.ui.screens.exercises.ExerciseViewModel
import ru.razumoff.razo.ui.screens.exercises.ExercisesCard
import ru.razumoff.razo.ui.screens.statistics.StatisticsCard
import ru.razumoff.razo.ui.screens.workouts.sessions.SessionCard
import ru.razumoff.razo.ui.screens.workouts.templates.TemplatesCard

@Composable
fun WorkoutScreen(
    viewModelWorkout: WorkoutViewModel = viewModel(),
    viewModelExercise: ExerciseViewModel = viewModel(),
    onTemplatesClick: () -> Unit,
    onExerciseClick: () -> Unit,
    onSessionClick: (WorkoutSession) -> Unit,
    onStatisticsClick: () -> Unit,
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
                        text = stringResource(R.string.loading_data),
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

                ExercisesCard(
                    exercisesCount = exercisesCount,
                    onClick = onExerciseClick
                )

                Spacer(modifier = Modifier.height(12.dp))

                TemplatesCard(
                    templatesCount = templatesCount,
                    onClick = onTemplatesClick
                )

                Spacer(modifier = Modifier.height(12.dp))

                StatisticsCard(
                    onClick = onStatisticsClick
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Заголовок раздела "Последние тренировки"
                Text(
                    text = if (sessions.isEmpty()) {
                        stringResource(R.string.recent_workouts)
                    } else {
                        stringResource(
                            R.string.recent_workouts_count,
                            sessions.size,
                            pluralStringResource(R.plurals.workouts_count, sessions.size)
                        )
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
                                imageVector = ImageVector.vectorResource(R.drawable.ic_barbell),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.no_completed_workouts),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = stringResource(R.string.start_first_workout_from_template),
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
            headerText = stringResource(R.string.workouts),
            actionDescription = stringResource(R.string.add),
            onActionClick = onTemplatesClick
        )
    }
}
