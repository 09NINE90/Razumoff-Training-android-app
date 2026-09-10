package ru.razumoff.razumofftraining.ui.screens.workouts.sessions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.dp
import ru.razumoff.razumofftraining.ui.components.headers.IslandWithButtonHeader
import ru.razumoff.razumofftraining.ui.screens.workouts.sessions.components.AddSetForm
import ru.razumoff.razumofftraining.ui.screens.workouts.sessions.components.SessionCompleted
import ru.razumoff.razumofftraining.ui.screens.workouts.sessions.components.SessionHeader
import ru.razumoff.razumofftraining.ui.screens.workouts.sessions.components.SessionNavigation
import ru.razumoff.razumofftraining.ui.screens.workouts.sessions.components.SetsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSessionScreen(
    viewModel: WorkoutSessionViewModel,
    onFinish: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    var repsInput by remember { mutableStateOf("") }
    var weightInput by remember { mutableStateOf("") }

    val currentExercise = viewModel.getCurrentExercise()
    val totalExercises = state.exercises.size
    val currentIndex = state.currentExerciseIndex

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .imePadding()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Подготовка тренировки...")
                    }
                }
            }
            state.isCompleted -> {
                SessionCompleted(
                    exercises = state.exercises,
                    onFinish = onFinish
                )
            }
            currentExercise == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет упражнений")
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 90.dp)
                        .imePadding()
                ) {
                    Spacer(modifier = Modifier.height(70.dp))

                    // Шапка: прогресс + название упражнения
                    SessionHeader(
                        currentIndex = currentIndex,
                        totalExercises = totalExercises,
                        exerciseName = currentExercise.exerciseName
                    )

                    // Список подходов
                    SetsList(
                        sets = currentExercise.sets,
                        onRemoveSet = { viewModel.removeSet(it) },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Форма добавления подхода
                    AddSetForm(
                        repsInput = repsInput,
                        onRepsChange = { repsInput = it },
                        weightInput = weightInput,
                        onWeightChange = { weightInput = it },
                        onAddSet = {
                            val reps = repsInput.toIntOrNull()
                            val weight = weightInput.toFloatOrNull()
                            if (reps != null && reps > 0) {
                                viewModel.addSet(reps, weight)
                            }
                        },
                        isEnabled = repsInput.isNotEmpty() &&
                                repsInput.toIntOrNull() != null &&
                                repsInput.toIntOrNull()!! > 0
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Кнопки навигации
                    SessionNavigation(
                        currentIndex = currentIndex,
                        totalExercises = totalExercises,
                        hasSets = currentExercise.sets.isNotEmpty(),
                        onPrevious = { viewModel.previousExercise() },
                        onNext = { viewModel.nextExercise() },
                        onFinish = { viewModel.finishSession(null) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Прогресс-бар
                    LinearProgressIndicator(
                        progress = { (currentIndex + 1).toFloat() / totalExercises },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Плавающий заголовок
        IslandWithButtonHeader(
            headerText = state.templateName.ifEmpty { "Тренировка" },
            onBackClick = {
                if (currentExercise?.sets?.isNotEmpty() == true) {
                    // TODO: Показать диалог подтверждения
                    onFinish()
                } else {
                    onFinish()
                }
            },
            showBackButton = true,
            showActionButton = false
        )
    }
}
