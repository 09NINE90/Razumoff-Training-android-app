package ru.razumoff.razumofftraining.ui.screens.workouts.session.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.razumoff.razumofftraining.R
import ru.razumoff.razumofftraining.ui.components.headers.IslandWithButtonHeader
import ru.razumoff.razumofftraining.ui.screens.workouts.session.SessionDetailViewModel
import ru.razumoff.razumofftraining.ui.screens.workouts.session.details.components.SessionExerciseDetailCard
import ru.razumoff.razumofftraining.ui.screens.workouts.session.details.components.SessionInfoCard
import ru.razumoff.razumofftraining.ui.screens.workouts.session.details.components.SessionInfoCopyButtons
import ru.razumoff.razumofftraining.utils.FormatUtils.exercises

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
                    text = "Тренировка не найдена",
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

                Spacer(modifier = Modifier.height(12.dp))

                // Кнопки копирования информации о сессии
                SessionInfoCopyButtons(
                    session = session,
                    modifier = modifier
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Список упражнений
                Text(
                    text = session!!.exercises.size.exercises(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(
                        items = session!!.exercises,
                        key = { _, item -> item.id }
                    ) { index, exercise ->
                        SessionExerciseDetailCard(
                            exercise = exercise,
                            modifier = Modifier.padding(
                                bottom = if (index == session!!.exercises.lastIndex) 130.dp else 0.dp
                            )
                        )
                    }
                }
            }
        }

        IslandWithButtonHeader(
            headerText = "Детали тренировки",
            onBackClick = onBack,
            showBackButton = true,
            actionIcon = ImageVector.vectorResource(R.drawable.ic_trash),
            actionDescription = "Удалить",
            onActionClick = { showDeleteDialog = true },
            actionButtonColor = MaterialTheme.colorScheme.error,
            actionIconTint = MaterialTheme.colorScheme.onError
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text("Удалить тренировку?")
            },
            text = {
                Text("Это действие нельзя отменить. Вся информация о тренировке будет удалена.")
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
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Отмена")
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
