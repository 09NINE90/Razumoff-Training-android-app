package ru.razumoff.razo.ui.screens.workouts.sessions.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.razumoff.razo.R
import ru.razumoff.razo.ui.components.headers.IslandWithButtonHeader
import ru.razumoff.razo.ui.screens.workouts.sessions.SessionDetailViewModel
import ru.razumoff.razo.ui.screens.workouts.sessions.details.components.SessionExerciseSets
import ru.razumoff.razo.ui.screens.workouts.sessions.details.components.SessionInfoCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDetailScreen(
    viewModel: SessionDetailViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val session by viewModel.session.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (session == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.workout_not_found),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(70.dp))

                // Информация о тренировке
                SessionInfoCard(
                    session = session!!,
                    onDateUpdate = { newDate ->
                        viewModel.updateDate(newDate)
                    }
                )

                // Список упражнений
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(
                        items = session!!.exercises,
                        key = { _, item -> item.id }
                    ) { index, exercise ->
                        SessionExerciseSets(
                            exercise=exercise,
                            modifier = Modifier.padding(
                                bottom = if (index == session!!.exercises.lastIndex) 50.dp else 0.dp
                            )
                        )
                    }
                }
            }
        }

        IslandWithButtonHeader(
            headerText = stringResource(R.string.workout_details),
            onBackClick = onBack,
            showBackButton = true,
            actionIcon = ImageVector.vectorResource(R.drawable.ic_trash),
            actionDescription = stringResource(R.string.delete),
            onActionClick = { showDeleteDialog = true }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(stringResource(R.string.delete_workout_question))
            },
            text = {
                Text(stringResource(R.string.delete_workout_confirmation))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteSession {
                            showDeleteDialog = false
                            onBack()
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        )
    }
}
