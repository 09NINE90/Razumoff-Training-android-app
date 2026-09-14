package ru.razumoff.razo.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.razumoff.razo.R

data class WorkoutTemplate(
    val id: String,
    val name: String,
    val description: String? = null,
    val exercises: List<TemplateExercise> = emptyList(),
    val workoutType: String = WorkoutType.REGULAR.name,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Composable
fun WorkoutTemplate.localizedWorkoutType(): String {
    return when (workoutType) {
        WorkoutType.REGULAR.name ->
            stringResource(R.string.regular_workout)

        WorkoutType.STRENGTH.name ->
            stringResource(R.string.strength_workout)

        else ->
            stringResource(R.string.regular_workout)
    }
}