package ru.razumoff.razo.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.razumoff.razo.R
import ru.razumoff.razo.ui.components.cards.SurfaceCard
import ru.razumoff.razo.ui.components.dialogs.DatePickerDialog
import ru.razumoff.razo.ui.components.headers.IslandWithButtonHeader
import ru.razumoff.razo.ui.components.inputs.EditableField
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Date

@Composable
fun UserProfileScreen(
    viewModel: UserViewModel = viewModel(),
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

    val dateFormat = SimpleDateFormat("dd.MM.yyyy", LocalLocale.current.platformLocale)

    @Composable
    fun calculateAge(birthDateMillis: Long?): String {
        if (birthDateMillis == null) return stringResource(R.string.not_specified)
        val birthDate =
            Date(birthDateMillis).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val today = LocalDate.now()
        val period = Period.between(birthDate, today)
        return pluralStringResource(
            R.plurals.years_count,
            period.years,
            period.years
        )
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
                    Text(
                        text = stringResource(R.string.user_not_found),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Аватар
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.medium
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
                SurfaceCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        // Имя
                        EditableField(
                            label = stringResource(R.string.name),
                            value = name,
                            isEditing = isEditing,
                            onValueChange = { name = it },
                            displayValue = user!!.name
                        )

                        // Возраст
                        EditableField(
                            label = stringResource(R.string.age),
                            value = birthDateString,
                            isEditing = isEditing,
                            onValueChange = {},
                            displayValue = calculateAge(user!!.birthDate),
                            placeholder = stringResource(R.string.date_of_birth),
                            trailingIcon = {
                                if (isEditing) {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_pencil_simple),
                                        contentDescription = stringResource(R.string.select_date),
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
                            label = stringResource(R.string.weight),
                            value = weight,
                            isEditing = isEditing,
                            onValueChange = { weight = it },
                            displayValue = user!!.weight?.let { "$it ${stringResource(R.string.kg)}" }
                                ?: stringResource(R.string.not_specified),
                            placeholder = "${stringResource(R.string.weight)} (${stringResource(R.string.kg)})"
                        )

                        // Рост
                        EditableField(
                            label = stringResource(R.string.height),
                            value = height,
                            isEditing = isEditing,
                            onValueChange = { height = it },
                            displayValue = user!!.height?.let { "$it ${stringResource(R.string.cm)}" }
                                ?: stringResource(R.string.not_specified),
                            placeholder = "${stringResource(R.string.height)} (${stringResource(R.string.cm)})"
                        )
                    }
                }

                if (isEditing) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { isEditing = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            }
        }

        // Плавающий заголовок
        IslandWithButtonHeader(
            headerText = stringResource(R.string.profile),
            showBackButton = false,
            actionIcon = if (isEditing) ImageVector.vectorResource(R.drawable.ic_floppy_disk) else ImageVector.vectorResource(
                R.drawable.ic_pencil_simple
            ),
            actionDescription = if (isEditing) stringResource(R.string.save) else stringResource(R.string.edit),
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
