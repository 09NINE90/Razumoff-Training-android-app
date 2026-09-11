package ru.razumoff.razumofftraining.ui.screens.workouts.templates.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import ru.razumoff.razumofftraining.R
import ru.razumoff.razumofftraining.models.WorkoutType
import ru.razumoff.razumofftraining.models.localizedName
import ru.razumoff.razumofftraining.ui.components.headers.IslandWithButtonHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTemplateScreen(
    viewModel: EditTemplateViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    onDeleted: () -> Unit
) {
    val templateName by viewModel.templateName.collectAsState()
    val templateDescription by viewModel.templateDescription.collectAsState()
    val workoutType by viewModel.workoutType.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

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
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 70.dp, bottom = 120.dp)
            ) {

                OutlinedTextField(
                    value = templateName,
                    onValueChange = viewModel::updateTemplateName,
                    label = {
                        Text("Название тренировки")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Тип тренировки",
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
                            onClick = {
                                viewModel.setWorkoutType(type)
                            },
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

                OutlinedTextField(
                    value = templateDescription,
                    onValueChange = viewModel::updateTemplateDescription,
                    label = {
                        Text("Описание")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSaving
                ) {
                    Text(stringResource(R.string.cancel))
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        showDeleteDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSaving
                ) {
                    Text("Удалить шаблон")
                }
            }

        }

        IslandWithButtonHeader(
            headerText = "Редактирование",
            onBackClick = onBack,
            showBackButton = true,
            showActionButton = true,
            enableActionButton = templateName.isNotBlank(),
            actionIcon = ImageVector.vectorResource(R.drawable.ic_floppy_disk),
            onActionClick = {
                viewModel.saveTemplate {
                    onSaved()
                }
            },
        )

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                },
                title = {
                    Text("Удалить шаблон?")
                },
                text = {
                    Text(
                        "Шаблон «$templateName» будет удалён.\n" +
                                "Это действие нельзя отменить."
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false

                            viewModel.deleteTemplate {
                                onDeleted()
                            }
                        },
                        enabled = !isSaving
                    ) {
                        Text("Удалить")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}
