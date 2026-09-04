package ru.razumoff.razumofftraining.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.razumoff.razumofftraining.ui.components.dialogs.DatePickerDialog
import ru.razumoff.razumofftraining.ui.components.headers.IslandWithButtonHeader
import ru.razumoff.razumofftraining.ui.components.inputs.EditableField
import ru.razumoff.razumofftraining.utils.FormatUtils.years
import ru.razumoff.razumofftraining.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Locale
import java.util.Date

@Composable
fun UserProfileScreen(
    viewModel: UserViewModel = viewModel(),
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Состояния для редактирования
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var birthDateString by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    // Состояние для DatePicker
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker
    )

    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    fun calculateAge(birthDateMillis: Long?): String {
        if (birthDateMillis == null) return "Не указан"
        val birthDate =
            Date(birthDateMillis).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val today = LocalDate.now()
        val period = Period.between(birthDate, today)
        return period.years.years()
    }

    LaunchedEffect(user) {
        user?.let {
            name = it.name
            birthDateString =
                it.birthDate?.let { dateMillis -> dateFormat.format(Date(dateMillis)) } ?: ""
            selectedDateMillis = it.birthDate
            it.birthDate?.let { datePickerState.selectedDateMillis = it }
            weight = it.weight?.toString() ?: ""
            height = it.height?.toString() ?: ""
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(70.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (user == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Пользователь не найден")
                }
            } else {
                // Аватар
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.extraLarge
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user!!.name.take(2).uppercase(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Информация о пользователе
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        // Имя
                        EditableField(
                            label = "Имя",
                            value = name,
                            isEditing = isEditing,
                            onValueChange = { name = it },
                            displayValue = user!!.name
                        )

                        // Возраст
                        EditableField(
                            label = "Возраст",
                            value = birthDateString,
                            isEditing = isEditing,
                            onValueChange = {},
                            displayValue = calculateAge(user!!.birthDate),
                            placeholder = "Дата рождения",
                            trailingIcon = {
                                if (isEditing) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Выбрать дату",
                                        modifier = Modifier.clickable {
                                            selectedDateMillis?.let {
                                                datePickerState.selectedDateMillis = it
                                            }
                                            showDatePicker = true
                                        }
                                    )
                                }
                            }
                        )

                        // Вес
                        EditableField(
                            label = "Вес",
                            value = weight,
                            isEditing = isEditing,
                            onValueChange = { weight = it },
                            displayValue = user!!.weight?.let { "$it кг" } ?: "Не указан",
                            placeholder = "Вес (кг)"
                        )

                        // Рост
                        EditableField(
                            label = "Рост",
                            value = height,
                            isEditing = isEditing,
                            onValueChange = { height = it },
                            displayValue = user!!.height?.let { "$it см" } ?: "Не указан",
                            placeholder = "Рост (см)"
                        )
                    }
                }

                if (isEditing) {
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { isEditing = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Отмена")
                    }
                }
            }
        }

        // Плавающий заголовок
        IslandWithButtonHeader(
            headerText = "Профиль",
            onBackClick = onBack,
            showBackButton = true,
            actionIcon = if (isEditing) Icons.Default.Save else Icons.Default.Edit,
            actionDescription = if (isEditing) "Сохранить" else "Редактировать",
            onActionClick = {
                if (isEditing) {
                    val birthDateToSave = selectedDateMillis ?: user?.birthDate
                    viewModel.updateUser(
                        name = name,
                        birthDate = birthDateToSave,
                        weight = weight.toFloatOrNull(),
                        height = height.toFloatOrNull()
                    )
                    isEditing = false
                } else {
                    isEditing = true
                }
            },
            actionButtonColor = if (isEditing) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.primaryContainer
            },
            actionIconTint = if (isEditing) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onPrimaryContainer
            }
        )
    }

    DatePickerDialog(
        showDialog = showDatePicker,
        onDismiss = {
            showDatePicker = false
            selectedDateMillis?.let {
                datePickerState.selectedDateMillis = it
            }
        },
        onDateSelected = { dateMillis ->
            selectedDateMillis = dateMillis
            birthDateString = dateFormat.format(Date(dateMillis))
        },
        initialDate = selectedDateMillis
    )

}