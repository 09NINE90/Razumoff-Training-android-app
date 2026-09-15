package ru.razumoff.razo.ui.screens.workouts.templates.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.razumoff.razo.R
import ru.razumoff.razo.models.TemplateExercise
import ru.razumoff.razo.ui.components.cards.SurfaceCard
import ru.razumoff.razo.ui.components.headers.IslandWithButtonHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateDetailScreen(
    viewModel: TemplateDetailViewModel = viewModel(),
    onBack: () -> Unit,
    onStartWorkout: () -> Unit,
    onEdit: () -> Unit
) {
    val template by viewModel.template.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(bottom = 40.dp)
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (template == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.template_not_found))
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(70.dp))

                // Количество упражнений
                Text(
                    text = pluralStringResource(
                        R.plurals.exercises_count,
                        template?.exercises?.size ?: 0,
                        template?.exercises?.size ?: 0
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Список упражнений
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = template?.exercises ?: emptyList(),
                        key = { it.exerciseId }
                    ) { exercise ->
                        TemplateExerciseItem(
                            exercise = exercise
                        )
                    }
                }

                // Кнопка "Начать тренировку"
                Button(
                    onClick = onStartWorkout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Text(stringResource(R.string.start_workout))
                }

                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Text(stringResource(R.string.back))
                }
            }
        }

        // Плавающий заголовок
        IslandWithButtonHeader(
            headerText = template?.name ?: "Детали шаблона",
            onBackClick = onBack,
            showBackButton = true,
            showActionButton = true,
            actionIcon = ImageVector.vectorResource(R.drawable.ic_pencil_simple),
            actionDescription = stringResource(R.string.edit),
            onActionClick = onEdit
        )
    }
}

@Composable
fun TemplateExerciseItem(
    exercise: TemplateExercise
) {
    SurfaceCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Номер
            Text(
                text = "${exercise.order}.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(28.dp)
            )

            // Название
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exercise.exerciseName,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
