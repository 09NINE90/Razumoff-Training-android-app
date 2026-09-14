package ru.razumoff.razo.ui.screens.exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.razumoff.razo.R
import ru.razumoff.razo.ui.components.cards.SurfaceCard
import ru.razumoff.razo.ui.components.headers.IslandWithButtonHeader

@Composable
fun ExercisesScreen(
    viewModel: ExerciseViewModel,
    navController: NavController,
    onAddExercise: () -> Unit,
    onBack: () -> Unit
) {
    val exercises by viewModel.exercises.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val exerciseSaved: String = stringResource(R.string.exercise_saved)

    val exerciseSavedState = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("exercise_saved", false)
        ?.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadExercises()
    }

    LaunchedEffect(exerciseSavedState?.value) {
        if (exerciseSavedState?.value == true) {
            snackbarHostState.showSnackbar(
                message = exerciseSaved,
                duration = SnackbarDuration.Short
            )

            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("exercise_saved", false)
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Long
            )
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            errorMessage?.let {
                SurfaceCard(
                    modifier = Modifier.padding(top = 80.dp),
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        text = "❌ $it",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (exercises.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_exercises),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(top = 70.dp)
                ) {
                    itemsIndexed(
                        items = exercises,
                        key = { _, item -> item.id }
                    ) { index, exercise ->
                        ExerciseCard(
                            exercise = exercise,
                            modifier = Modifier.padding(
                                bottom = if (index == exercises.lastIndex) 110.dp else 0.dp
                            )
                        )
                    }
                }
            }
        }

        // 2. Плавающая шапка (поверх всего)
        IslandWithButtonHeader(
            headerText = stringResource(R.string.exercises),
            actionDescription = stringResource(R.string.add_exercise),
            onActionClick = onAddExercise,
            onBackClick = onBack,
            showBackButton = true
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 110.dp)
        )
    }
}
