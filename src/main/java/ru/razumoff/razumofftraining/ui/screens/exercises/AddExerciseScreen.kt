package ru.razumoff.razumofftraining.ui.screens.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.razumoff.razumofftraining.models.*
import ru.razumoff.razumofftraining.ui.components.buttons.ActionButtons
import ru.razumoff.razumofftraining.ui.components.chips.FilterChipGroup
import ru.razumoff.razumofftraining.ui.components.dropdown.MultiSelectDropdown
import ru.razumoff.razumofftraining.ui.components.inputs.AppTextField
import ru.razumoff.razumofftraining.ui.components.sections.SectionHeader
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseScreen(
    onSave: (Exercise) -> Unit,
    onCancel: () -> Unit,
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(bottom = 100.dp)
            .verticalScroll(scrollState)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
        Text(
            text = "Новое упражнение",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Название
        AppTextField(
            value = name,
            onValueChange = { name = it },
            label = "Название *",
            isError = name.isBlank()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Описание
        AppTextField(
            value = description,
            onValueChange = { description = it },
            label = "Описание",
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
            label = "Группы мышц",
            displayMapper = { it.displayName }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Часть тела
        SectionHeader(title = "Часть тела")
        Spacer(modifier = Modifier.height(4.dp))

        FilterChipGroup(
            items = BodyPart.entries,
            selectedItem = selectedBodyPart,
            onItemSelected = { selectedBodyPart = it },
            labelMapper = { it.displayName }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Тип движения
        SectionHeader(title = "Тип движения")
        Spacer(modifier = Modifier.height(4.dp))

        FilterChipGroup(
            items = MovementType.entries,
            selectedItem = selectedMovementType,
            onItemSelected = { selectedMovementType = it },
            labelMapper = { it.displayName }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Оборудование
        AppTextField(
            value = equipment,
            onValueChange = { equipment = it },
            label = "Оборудование (необязательно)",
            placeholder = "Штанга, Гантели, Блочный тренажер",
            imeAction = ImeAction.Next,
            onImeAction = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Заметки
        AppTextField(
            value = notes,
            onValueChange = { notes = it },
            label = "Заметки (необязательно)",
            minLines = 2,
            singleLine = false,
            imeAction = ImeAction.Done,
            onImeAction = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопки
        ActionButtons(
            onCancel = {
                keyboardController?.hide()
                focusManager.clearFocus()
                onCancel()
            },
            onSave = {
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
            isSaveEnabled = name.isNotBlank() && selectedMuscleGroups.isNotEmpty() && !isSaving,
            saveText = if (isSaving) "Сохранение..." else "Сохранить"
        )

        // Подсказка о заполнении
        if (name.isNotBlank() && selectedMuscleGroups.isEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Выберите хотя бы одну группу мышц",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}