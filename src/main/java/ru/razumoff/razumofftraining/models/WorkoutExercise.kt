package ru.razumoff.razumofftraining.models

data class WorkoutExercise(
    val id: String,                     // ID записи в workout_exercises
    val exerciseId: String,             // ID упражнения
    val exerciseName: String = "",      // Денормализованное поле для отображения
    val order: Int,
    val sets: List<WorkoutSet>,
    val notes: String? = null
)