package ru.razumoff.razo.ui.screens.user

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DisplayMode
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import ru.razumoff.razo.models.MeasurementType
import ru.razumoff.razo.ui.components.dialogs.DatePickerDialog
import ru.razumoff.razo.ui.components.headers.IslandWithButtonHeader
import ru.razumoff.razo.ui.screens.user.components.ProfileEditContent
import ru.razumoff.razo.ui.screens.user.components.ProfileViewContent
import ru.razumoff.razo.utils.enterFadeInExpandVerticallyTransition
import ru.razumoff.razo.utils.exitFadeOutShrinkVerticallyTransition
import ru.razumoff.razo.utils.profileContentTransition
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
    val measurements by viewModel.measurements.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Состояния для редактирования
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var birthDateString by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    // Состояние для DatePicker
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker
    )

    var measurementInputs by rememberSaveable {
        mutableStateOf<Map<MeasurementType, String>>(emptyMap())
    }

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
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    LaunchedEffect(isEditing, measurements) {
        if (isEditing) {
            measurementInputs = MeasurementType.entries.associateWith { type ->
                measurements[type]?.value?.toString().orEmpty()
            }
        }
    }

    val measurementTypes = MeasurementType.entries

    val userSummary = if (user!!.birthDate != null) {
        "${user!!.name}, ${calculateAge(user!!.birthDate)}"
    } else {
        user!!.name
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {

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
                AnimatedVisibility(
                    visible = !isEditing,
                    enter = enterFadeInExpandVerticallyTransition(),
                    exit = exitFadeOutShrinkVerticallyTransition()
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
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
                }

                // Информация о пользователе
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedContent(
                        targetState = isEditing,
                        transitionSpec = {
                            profileContentTransition()
                        },
                        label = "profileContent"
                    ) { editing ->

                        if (editing) {
                            ProfileEditContent(
                                name = name,
                                birthDateString = birthDateString,
                                measurementTypes = measurementTypes,
                                measurementInputs = measurementInputs,
                                onNameChange = { name = it },
                                onMeasurementChange = { type, value ->
                                    measurementInputs = measurementInputs.toMutableMap().apply {
                                        this[type] = value
                                    }
                                },
                                onSelectDate = {
                                    selectedDateMillis?.let {
                                        datePickerState.selectedDateMillis = it
                                    }
                                    showDatePicker = true
                                }
                            )

                        } else {
                            ProfileViewContent(
                                userSummary = userSummary,
                                measurementTypes = measurementTypes,
                                measurements = measurements
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = isEditing,
                    enter = enterFadeInExpandVerticallyTransition(),
                    exit = exitFadeOutShrinkVerticallyTransition()
                ) {
                    Button(
                        onClick = { isEditing = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 12.dp
                            )
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

                    viewModel.saveProfile(
                        name = name,
                        birthDate = birthDateToSave,
                        measurements = measurementInputs
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
