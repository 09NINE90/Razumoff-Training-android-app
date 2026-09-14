package ru.razumoff.razo.ui.screens.workouts.templates.create

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.razumoff.razo.R
import ru.razumoff.razo.models.WorkoutType
import ru.razumoff.razo.models.localizedName
import ru.razumoff.razo.ui.components.cards.SurfaceCard
import ru.razumoff.razo.ui.components.headers.IslandWithButtonHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTemplateScreen(
    viewModel: CreateTemplateViewModel,
    onBack: () -> Unit,
    onTemplateCreated: () -> Unit = {}
) {
    val templateName by viewModel.templateName.collectAsState()
    val templateDescription by viewModel.templateDescription.collectAsState()
    val selectedExercises by viewModel.selectedExercises.collectAsState()
    val availableExercises by viewModel.availableExercises.collectAsState()
    val workoutType by viewModel.workoutType.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Состояние для поиска
    var searchQuery by remember { mutableStateOf("") }

    // Фильтруем упражнения по поисковому запросу
    val filteredExercises = remember(searchQuery, availableExercises) {
        if (searchQuery.isEmpty()) {
            availableExercises
        } else {
            availableExercises.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadExercises()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(70.dp))

            // Название
            OutlinedTextField(
                value = templateName,
                onValueChange = { viewModel.updateTemplateName(it) },
                label = { Text(stringResource(R.string.workout_name)) },
                placeholder = { Text(stringResource(R.string.workout_name_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = templateName.isBlank(),
                supportingText = {
                    if (templateName.isBlank()) {
                        Text(
                            text = stringResource(R.string.required_field),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.workout_type),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                WorkoutType.entries.forEachIndexed { index, type ->
                    SegmentedButton(
                        selected = workoutType == type,
                        onClick = { viewModel.setWorkoutType(type) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = WorkoutType.entries.size
                        ),
                        icon = {
                            SegmentedButtonDefaults.Icon(
                                active = workoutType == type,
                                activeContent = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_check),
                                        contentDescription = null
                                    )
                                },
                                inactiveContent = {}
                            )
                        }
                    ) {
                        Text(type.localizedName())
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Строка поиска
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text(stringResource(R.string.search_exercises)) },
                placeholder = { Text(stringResource(R.string.enter_exercise_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(R.string.search)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(R.string.clear),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Список упражнений
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (availableExercises.isEmpty()) {
                SurfaceCard(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.no_exercises_available),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = stringResource(R.string.create_exercises_in_section),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else if (filteredExercises.isEmpty()) {
                // Нет результатов поиска
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = stringResource(R.string.nothing_found),
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.nothing_found),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = stringResource(R.string.try_different_query),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredExercises) { exercise ->
                        val isAdded = selectedExercises.any { it.exerciseId == exercise.id }
                        ExerciseSelectItem(
                            exercise = exercise,
                            isAdded = isAdded,
                            onAdd = { viewModel.addExercise(exercise) }
                        )
                    }
                }
            }

            // Выбранные упражнения (над строкой поиска)
            if (selectedExercises.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp),
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = pluralStringResource(
                        R.plurals.selected_exercises_count,
                        selectedExercises.size,
                        selectedExercises.size
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Список выбранных упражнений
                selectedExercises.forEachIndexed { index, exercise ->
                    SelectedExerciseItem(
                        exercise = exercise,
                        onRemove = { viewModel.removeExercise(exercise.exerciseId) }
                    )
                    if (index < selectedExercises.lastIndex) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

            }

            Spacer(modifier = Modifier.height(120.dp))
        }

        // Плавающий заголовок (без кнопки добавления)
        IslandWithButtonHeader(
            headerText = stringResource(R.string.new_template),
            actionDescription = stringResource(R.string.save),
            actionIcon = ImageVector.vectorResource(R.drawable.ic_floppy_disk),
            onActionClick = {
                viewModel.saveTemplate {
                    onTemplateCreated()
                    onBack()
                }
            },
            onBackClick = onBack,
            showBackButton = true,
            enableActionButton = selectedExercises.isNotEmpty() && templateName.isNotEmpty(),
            showActionButton = true
        )
    }
}

