package ru.razumoff.razumofftraining.ui.screens.templates

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.razumoff.razumofftraining.ui.components.headers.IslandWithButtonHeader
import ru.razumoff.razumofftraining.utils.FormatUtils.exercises

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
                label = { Text("Название тренировки") },
                placeholder = { Text("Например: Верх тела") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = templateName.isBlank(),
                supportingText = {
                    if (templateName.isBlank()) {
                        Text("Обязательное поле", color = MaterialTheme.colorScheme.error)
                    }
                },
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Описание
//            OutlinedTextField(
//                value = templateDescription,
//                onValueChange = { viewModel.updateTemplateDescription(it) },
//                label = { Text("Описание (необязательно)") },
//                placeholder = { Text("Коротко о тренировке") },
//                modifier = Modifier.fillMaxWidth(),
//                minLines = 2,
//                maxLines = 4
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))

            // Строка поиска
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Поиск упражнений") },
                placeholder = { Text("Введите название упражнения...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Поиск")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Очистить",
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
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
                            text = "Нет упражнений",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Создайте упражнения в разделе Упражнения",
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
                            contentDescription = "Ничего не найдено",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ничего не найдено",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Попробуйте изменить запрос",
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

                // Сохранить
                Button(
                    onClick = {
                        viewModel.saveTemplate()
                        onTemplateCreated()
                        onBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    enabled = !isSaving && templateName.isNotBlank() && selectedExercises.isNotEmpty(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Сохранить",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Сохранить шаблон")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Выбрано (${selectedExercises.size.exercises()})",
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
            headerText = "Новый шаблон",
            actionDescription = "Сохранить",
            onActionClick = {
                viewModel.saveTemplate()
                onTemplateCreated()
                onBack()
            },
            onBackClick = onBack,
            showBackButton = true,
            showActionButton = false
        )
    }
}

