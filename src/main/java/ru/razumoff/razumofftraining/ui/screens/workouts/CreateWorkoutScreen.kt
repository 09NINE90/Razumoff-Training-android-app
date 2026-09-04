//package ru.razumoff.razumofftraining.ui.screens.workouts
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Check
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import ru.razumoff.razumofftraining.models.Exercise
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CreateWorkoutScreen(
//    viewModel: CreateWorkoutViewModel,
//    onBack: () -> Unit
//) {
//    val state by viewModel.state.collectAsState()
//    val availableExercises by viewModel.availableExercises.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val isSaving by viewModel.isSaving.collectAsState()
//    val errorMessage by viewModel.errorMessage.collectAsState()
//    val saveSuccess by viewModel.saveSuccess.collectAsState()
//
//    val snackbarHostState = remember { SnackbarHostState() }
//    val scrollState = rememberScrollState()
//
//    // Реакция на успешное сохранение
//    LaunchedEffect(saveSuccess) {
//        if (saveSuccess) {
//            snackbarHostState.showSnackbar("Тренировка сохранена!")
//            onBack()
//        }
//    }
//
//    // Реакция на ошибку
//    LaunchedEffect(errorMessage) {
//        errorMessage?.let {
//            snackbarHostState.showSnackbar(it)
//        }
//    }
//
//    Scaffold(
//        snackbarHost = { SnackbarHost(snackbarHostState) },
//        topBar = {
//            TopAppBar(
//                title = { Text("Новая тренировка") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.Close, contentDescription = "Закрыть")
//                    }
//                },
//                actions = {
//                    Button(
//                        onClick = { viewModel.saveWorkout() },
//                        enabled = !isSaving && state.selectedExercises.isNotEmpty()
//                    ) {
//                        if (isSaving) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.size(20.dp),
//                                color = MaterialTheme.colorScheme.onPrimary
//                            )
//                        } else {
//                            Icon(Icons.Default.Check, contentDescription = "Сохранить")
//                            Spacer(modifier = Modifier.width(4.dp))
//                            Text("Сохранить")
//                        }
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(16.dp)
//                .verticalScroll(scrollState),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            // Выбор упражнений
//            Text(
//                text = "Добавить упражнение",
//                style = MaterialTheme.typography.titleMedium
//            )
//
//            // Список доступных упражнений для добавления
//            if (availableExercises.isNotEmpty()) {
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(120.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    items(availableExercises) { exercise ->
//                        val alreadyAdded = state.selectedExercises.any { it.exercise.id == exercise.id }
//                        ExerciseChip(
//                            exercise = exercise,
//                            isAdded = alreadyAdded,
//                            onAdd = {
//                                if (!alreadyAdded) {
//                                    viewModel.addExercise(exercise)
//                                }
//                            }
//                        )
//                    }
//                }
//            } else if (isLoading) {
//                Box(
//                    modifier = Modifier.fillMaxWidth().height(120.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator()
//                }
//            } else {
//                Card(
//                    modifier = Modifier.fillMaxWidth().height(120.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.surfaceVariant
//                    )
//                ) {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "Нет упражнений. Создайте их в разделе Упражнения",
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                    }
//                }
//            }
//
//            // Список добавленных упражнений
//            if (state.selectedExercises.isNotEmpty()) {
//                Text(
//                    text = "Упражнения в тренировке (${state.selectedExercises.size})",
//                    style = MaterialTheme.typography.titleMedium
//                )
//
//                state.selectedExercises.forEach { workoutExercise ->
//                    WorkoutExerciseCard(
//                        workoutExercise = workoutExercise,
//                        onRemove = { viewModel.removeExercise(workoutExercise.exercise.id) },
//                        onAddSet = { viewModel.addSet(workoutExercise.exercise.id) },
//                        onRemoveSet = { set -> viewModel.removeSet(workoutExercise.exercise.id, set) },
//                        onUpdateSet = { set, reps, weight, isWarmup ->
//                            viewModel.updateSet(
//                                workoutExercise.exercise.id,
//                                set,
//                                reps,
//                                weight,
//                                isWarmup
//                            )
//                        }
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                }
//            }
//
//            // Дополнительная информация
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                ) {
//                    Text(
//                        text = "Дополнительно",
//                        style = MaterialTheme.typography.titleMedium
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    // Длительность
//                    OutlinedTextField(
//                        value = state.duration?.toString() ?: "",
//                        onValueChange = {
//                            val value = it.toIntOrNull()
//                            viewModel.updateDuration(value)
//                        },
//                        label = { Text("Длительность (минуты)") },
//                        modifier = Modifier.fillMaxWidth(),
//                        placeholder = { Text("Например: 60") }
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    // Заметки
//                    OutlinedTextField(
//                        value = state.notes,
//                        onValueChange = { viewModel.updateNotes(it) },
//                        label = { Text("Заметки") },
//                        modifier = Modifier.fillMaxWidth(),
//                        minLines = 2,
//                        placeholder = { Text("Как прошла тренировка?") }
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//        }
//    }
//}
//
//@Composable
//fun ExerciseChip(
//    exercise: Exercise,
//    isAdded: Boolean,
//    onAdd: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = if (isAdded) {
//                MaterialTheme.colorScheme.primaryContainer
//            } else {
//                MaterialTheme.colorScheme.surface
//            }
//        )
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column {
//                Text(
//                    text = exercise.name,
//                    style = MaterialTheme.typography.titleSmall
//                )
//                Text(
//                    text = exercise.muscleGroups.joinToString(", ") { it.displayName },
//                    fontSize = 12.sp,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//
//            if (isAdded) {
//                Icon(
//                    Icons.Default.Check,
//                    contentDescription = "Добавлено",
//                    tint = MaterialTheme.colorScheme.primary
//                )
//            } else {
//                IconButton(
//                    onClick = onAdd,
//                    modifier = Modifier.size(32.dp)
//                ) {
//                    Icon(
//                        Icons.Default.Add,
//                        contentDescription = "Добавить"
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun WorkoutExerciseCard(
//    workoutExercise: CreateWorkoutExercise,
//    onRemove: () -> Unit,
//    onAddSet: () -> Unit,
//    onRemoveSet: (CreateWorkoutSet) -> Unit,
//    onUpdateSet: (CreateWorkoutSet, Int?, Float?, Boolean?) -> Unit
//) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = workoutExercise.exercise.name,
//                    style = MaterialTheme.typography.titleMedium
//                )
//
//                IconButton(onClick = onRemove) {
//                    Icon(
//                        Icons.Default.Delete,
//                        contentDescription = "Удалить упражнение",
//                        tint = MaterialTheme.colorScheme.error
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(4.dp))
//
//            // Подходы
//            workoutExercise.sets.forEach { set ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "${set.setNumber}.",
//                        fontSize = 14.sp,
//                        modifier = Modifier.width(24.dp)
//                    )
//
//                    OutlinedTextField(
//                        value = if (set.reps == 0) "" else set.reps.toString(),
//                        onValueChange = {
//                            val reps = it.toIntOrNull()
//                            onUpdateSet(set, reps, null, null)
//                        },
//                        label = { Text("Повт") },
//                        modifier = Modifier.weight(1f),
//                        singleLine = true
//                    )
//
//                    OutlinedTextField(
//                        value = if (set.weight == 0f) "" else set.weight.toString(),
//                        onValueChange = {
//                            val weight = it.toFloatOrNull()
//                            onUpdateSet(set, null, weight, null)
//                        },
//                        label = { Text("Вес") },
//                        modifier = Modifier.weight(1f),
//                        singleLine = true
//                    )
//
//                    // Разминочный подход
//                    FilterChip(
//                        selected = set.isWarmup,
//                        onClick = {
//                            onUpdateSet(set, null, null, !set.isWarmup)
//                        },
//                        label = { Text("W") },
//                        modifier = Modifier.width(56.dp)
//                    )
//
//                    IconButton(
//                        onClick = { onRemoveSet(set) },
//                        modifier = Modifier.size(32.dp)
//                    ) {
//                        Icon(
//                            Icons.Default.Close,
//                            contentDescription = "Удалить подход",
//                            tint = MaterialTheme.colorScheme.error,
//                            modifier = Modifier.size(16.dp)
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Кнопка добавления подхода
//            Button(
//                onClick = onAddSet,
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.secondaryContainer
//                )
//            ) {
//                Icon(Icons.Default.Add, contentDescription = "Добавить подход")
//                Spacer(modifier = Modifier.width(4.dp))
//                Text("Добавить подход")
//            }
//        }
//    }
//}