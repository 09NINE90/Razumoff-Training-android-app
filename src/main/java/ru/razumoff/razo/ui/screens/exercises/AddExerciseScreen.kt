package ru.razumoff.razo.ui.screens.exercises

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.razumoff.razo.R
import ru.razumoff.razo.models.BodyPart
import ru.razumoff.razo.models.Exercise
import ru.razumoff.razo.models.MovementType
import ru.razumoff.razo.models.MuscleGroup
import ru.razumoff.razo.models.localizedName
import ru.razumoff.razo.ui.components.chips.FilterChipGroup
import ru.razumoff.razo.ui.components.dropdown.MultiSelectDropdown
import ru.razumoff.razo.ui.components.headers.IslandWithButtonHeader
import ru.razumoff.razo.ui.components.inputs.AppTextField
import ru.razumoff.razo.ui.components.sections.SectionHeader
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseScreen(
    onSave: (Exercise) -> Unit,
    onCancel: () -> Unit,
    onBack: () -> Unit,
    isSaving: Boolean = false
) {
    // Состояния
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedMuscleGroups by remember { mutableStateOf<Set<MuscleGroup>>(emptySet()) }
    var selectedBodyPart by remember { mutableStateOf(BodyPart.UPPER) }
    var selectedMovementType by remember { mutableStateOf(MovementType.COMPOUND) }
    var equipment by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var muscleGroupDropdownExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)
                .verticalScroll(scrollState)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Название
            AppTextField(
                value = name,
                onValueChange = { name = it },
                label = stringResource(R.string.exercise_name),
                isError = name.isBlank()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Описание
            AppTextField(
                value = description,
                onValueChange = { description = it },
                label = stringResource(R.string.exercise_description),
                minLines = 2,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Группы мышц (Dropdown)
            MultiSelectDropdown(
                items = MuscleGroup.entries,
                selectedItems = selectedMuscleGroups,
                onItemToggle = { muscle ->
                    selectedMuscleGroups = if (muscle in selectedMuscleGroups) {
                        selectedMuscleGroups - muscle
                    } else {
                        selectedMuscleGroups + muscle
                    }
                },
                expanded = muscleGroupDropdownExpanded,
                onExpandedChange = { muscleGroupDropdownExpanded = it },
                label = stringResource(R.string.exercise_muscle_groups),
                displayMapper = { it.localizedName() }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Часть тела
            SectionHeader(title = stringResource(R.string.exercise_body_part))
            Spacer(modifier = Modifier.height(4.dp))

            FilterChipGroup(
                items = BodyPart.entries,
                selectedItem = selectedBodyPart,
                onItemSelected = { selectedBodyPart = it },
                labelMapper = { it.localizedName() }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Тип движения
            SectionHeader(title = stringResource(R.string.exercise_movement_type))
            Spacer(modifier = Modifier.height(4.dp))

            FilterChipGroup(
                items = MovementType.entries,
                selectedItem = selectedMovementType,
                onItemSelected = { selectedMovementType = it },
                labelMapper = { it.localizedName() }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Оборудование
            AppTextField(
                value = equipment,
                onValueChange = { equipment = it },
                label = stringResource(R.string.exercise_equipment_optional),
                placeholder = "${stringResource(R.string.exercise_equipment_barbell)}, " +
                        "${stringResource(R.string.exercise_equipment_dumbbells)}, " +
                        stringResource(R.string.exercise_equipment_cable_machine),
                imeAction = ImeAction.Next,
                onImeAction = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Заметки
            AppTextField(
                value = notes,
                onValueChange = { notes = it },
                label = stringResource(R.string.exercise_notes_optional),
                minLines = 2,
                singleLine = false,
                imeAction = ImeAction.Done,
                onImeAction = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.cancel))
            }

            // Подсказка о заполнении
            if (name.isNotBlank() && selectedMuscleGroups.isEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.select_at_least_one_muscle_group),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        IslandWithButtonHeader(
            headerText = stringResource(R.string.new_exercise),
            actionDescription = stringResource(R.string.add_exercise),
            actionIcon = ImageVector.vectorResource(R.drawable.ic_floppy_disk),
            enableActionButton = name.isNotBlank() && selectedMuscleGroups.isNotEmpty() && !isSaving,
            onActionClick = {
                keyboardController?.hide()
                focusManager.clearFocus()
                val exercise = Exercise(
                    id = UUID.randomUUID().toString(),
                    name = name.trim(),
                    description = description.trim(),
                    muscleGroups = selectedMuscleGroups.toList(),
                    bodyPart = selectedBodyPart,
                    movementType = selectedMovementType,
                    equipment = equipment.takeIf { it.isNotBlank() }?.trim(),
                    notes = notes.takeIf { it.isNotBlank() }?.trim(),
                    isCustom = true
                )
                onSave(exercise)
            },
            onBackClick = onBack,
            showBackButton = true
        )
    }
}